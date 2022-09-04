package com.example.intern_vnpttech_libmanagement.serviceimpls.file_process;

import com.example.intern_vnpttech_libmanagement.dto.info.CellInfos;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.entities.BookType;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.BookTypeRepo;
import com.example.intern_vnpttech_libmanagement.repositories.PublisherRepo;
import com.example.intern_vnpttech_libmanagement.utilities.StringProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class ExcelFileService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private BookTypeRepo bookTypeRepo;

    @Autowired
    private PublisherRepo publisherRepo;


    /*
    * Convert multipart file to a file
    * */
    public File convertMultipartToFile(MultipartFile multipartFile)
    {
        try {
            File outputFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return outputFile;
        } catch (Exception ex)
        {
            log.error("Open file error",ex);
            return null;
        }
    }

    /*
    * Validate input multipart file : file , filename's ex, filename's size, file's size
    * */
    public boolean validateExcelFile(MultipartFile file)
    {
        try{
            if(file==null) return false;
            String fileName = file.getOriginalFilename();
            if(StringUtils.hasText(fileName))
            {
                // check filename's length
                if(fileName.length()<5)
                {
                    log.error("File name is too short");
                    return false;
                }
                else if(fileName.length()>2000)
                {
                    log.error("File name is too long");
                    return false;
                }
                // check filename's extentions
                String ex= fileName.substring(fileName.trim().lastIndexOf("."));
                if(ex.isBlank() ||!ex.contains("xls") || !ex.contains("xlsx")) // xls and xlsx are MS excel file types
                {
                    log.error("File type error");
                    return false;
                }
                // Check file's size
                if(file.getSize()>10*1024*1024L) // max file size if 10MB
                {
                    log.error("File's size exceeded maximum allowed size");
                    return false;
                }
            } else {
                log.error("File name is blank");
                return false;
            }
            return true;
        }catch (Exception ex)
        {
            log.error("Validate file error",ex);
            return false;
        }
    }

    /*
    * Get workbook with  particular type : xls -> HSSF , xlsx -> XSSF
    * */
    public Workbook getWorkBook(MultipartFile multipartFile)
    {
        try{
            File excelImportFile = convertMultipartToFile(multipartFile);
            FileInputStream fis = new FileInputStream(excelImportFile);
            String filePath = multipartFile.getOriginalFilename();
            String fileEx = filePath.trim().substring(filePath.lastIndexOf("."));
            if(fileEx.equals(".xls")) return new HSSFWorkbook(fis);
            else if(fileEx.equals(".xlsx")) return new XSSFWorkbook(fis);
            else throw new RuntimeException("File type error");
        } catch (Exception ex)
        {
            log.error("Get work book error",ex);
            return null;
        }
    }

    /*
    * Get cell's value with particular type
    * */
    public Object getCellValue(Cell cell)
    {
        Object cellValue = null;
        switch (cell.getCellType()) {
            case BOOLEAN: {
                cellValue = cell.getBooleanCellValue();
                break;
            }
            case STRING: {
                cellValue = cell.getStringCellValue();
                break;
            }
            case NUMERIC:{
                cellValue =cell.getNumericCellValue();
                break;
            }
            case FORMULA:{
                cellValue =cell.getCellFormula();
                break;
            }
            default: {
                break;
            }
        }
        return cellValue;
    }

    /*
    * Process excel file to import books to database
    * */
    public void importBookFromExcelFile(MultipartFile excelFile)
    {
        try{
            log.trace("Call importBookFromExcelFile method");
            if(!validateExcelFile(excelFile))
                throw new RuntimeException("Input file is not accepted !");
            Workbook workbook = getWorkBook(excelFile);
            Sheet firstSheet = workbook.getSheetAt(0);
            Map<String,Integer> columnNames = new HashMap<>();
            //Set<CellInfos> cellInfos = new HashSet<>();
            Iterator<Row> iter = firstSheet.iterator();
            //
            while (iter.hasNext())
            {
                Row row = iter.next();
                // check if row is empty
                if(row.getCell(columnNames.get("Num")).toString().equals("") || row.getCell(columnNames.get("Num")) ==null)
                    break;
                // Get first row -> column's name and column index to map
                if(row.getRowNum()==0) {
                    for (Cell cell : row) {
                        if (getCellValue(cell) instanceof String) {
                            columnNames.put(getCellValue(cell).toString(), cell.getColumnIndex());
                            //  cellInfos.add(new CellInfos(row.getRowNum(),cell.getColumnIndex(),getCellValue(cell).toString()));
                        }
                    }
                    continue;
                }
                // get book's infos from a row ;
                Book newBook = new Book();
                String bookName = getCellValue(row.getCell(columnNames.get("Book Name"))).toString();
                String authorName = getCellValue(row.getCell(columnNames.get("Author Name"))).toString();
                String bookType = getCellValue(row.getCell(columnNames.get("Book Type"))).toString();
                String publisher = getCellValue(row.getCell(columnNames.get("Publisher"))).toString();
                long publishingYear = Math.round(Double.parseDouble(getCellValue(row.getCell(columnNames.get("Publishing Year"))).toString()));
                long amount = Math.round(Double.parseDouble(getCellValue(row.getCell(columnNames.get("Amount"))).toString()));
                String bookImage = getCellValue(row.getCell(columnNames.get("Book Image"))).toString();
                String bookCode = StringProcessUtils.bookCodeGenerator(bookType,bookName,authorName,(int)publishingYear);

                // check if the booktype is existed in db
                if(!bookTypeRepo.getAllBookType().stream().filter(bookType1 -> bookType1.getBookTypeName().equals(bookType)).findFirst().isPresent())
                {
                   BookType newBookType = bookTypeRepo
                           .save(BookType.builder()
                                            .bookTypeName(bookType)
                                            .createdAt(new Timestamp(System.currentTimeMillis()))
                                            .deleted(false).build());
                   newBook.setBookType(newBookType);
                } else newBook.setBookType(bookTypeRepo
                                .getAllBookType()
                                .stream()
                                .filter(bookType1 -> bookType1.getBookTypeName().equals(bookType)).findFirst().get());

                // check if  the publisher is existed in db
                if(publisherRepo.existedPublisher(publisher)<=0){
                    Publisher newPublisher = Publisher.builder().publisherName(publisher).build();
                    Publisher addedPublisher = publisherRepo.save(newPublisher);
                    if(addedPublisher ==null) {
                        log.trace("Add new publisher in importBook method ");
                        throw new RuntimeException("Add new publisher error");
                    } else newBook.setPublisher(addedPublisher);
                } else {
                    newBook.setPublisher(publisherRepo.findByPublisherName(publisher).get(0));
                }
                newBook.setBookCode(bookCode);
                newBook.setBookName(bookName);
                newBook.setBookPublishYear((int) publishingYear);
                newBook.setDeleted(false);
                newBook.setBookAuthor(authorName.toUpperCase());
                newBook.setBookState("Good");
                newBook.setAddedAt(new Timestamp(System.currentTimeMillis()));
                newBook.setBookImage(bookImage);
                newBook.setAvailable(true);
                for(int i =1 ; i<= amount; i++)
                {
                    Book addedBook = (Book) newBook.clone(); // clone a record of a book
                    if(bookRepo.save(addedBook) == null)
                        continue;
                }
            }
        } catch (Exception ex)
        {
            log.error("Import book to database error",ex);
            ex.printStackTrace();
        }
    }
}
