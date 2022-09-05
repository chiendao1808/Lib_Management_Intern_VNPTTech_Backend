package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.request.BodyRequest;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BookRecordDTO;
import com.example.intern_vnpttech_libmanagement.dto.info.NewBookDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.entities.BookType;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.repositories.BookTypeRepo;
import com.example.intern_vnpttech_libmanagement.serviceimpls.file_process.ExcelFileService;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import com.example.intern_vnpttech_libmanagement.services.BookTypeService;
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping(path = "/libmng/api/book")
@Tag(name = "Book Controller")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private ExcelFileService excelFileService;

    @Autowired
    private BookTypeRepo bookTypeRepo;

    // Gộp tất cả api get thành 1 API getAll : truyền tất cả các params cần thiết

    /*
    * Filter API for book
    */
    @Operation(summary = "Get books with a criteria")
    @GetMapping(path = "/get")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> get(@RequestParam(name = "book_id",required = false,defaultValue = "0")Long bookId,
                                 @RequestParam(name = "book_name",required = false,defaultValue = "null") String bookName,
                                 @RequestParam(name = "book_author",required = false,defaultValue = "null") String bookAuthor,
                                 @RequestParam(name = "book_code",required = false,defaultValue = "null")String bookCode,
                                 @RequestParam(name = "publisher_id",required = false,defaultValue = "0") Long publisherId,
                                 @RequestParam(name = "all",required = false,defaultValue = "false") Boolean all,
                                 @RequestParam(name = "page",defaultValue = "1") Integer page,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable =PageRequest.of(page-1,2);
        if(all.booleanValue()==true)
            return ResponseEntity.ok(bookService.findAll(pageable));
        return ResponseEntity.ok(bookService.findByCriteria(bookId,bookName,bookAuthor,bookCode,publisherId,all,pageable));
    }

    @Operation(summary = "Find all book's records from database")
    @SecurityRequirement(name = "methodAuth")
    @GetMapping(path = "/find-all-rec")
    @Parameters({@Parameter(name = "page",description = "page number for paging")})
    public ResponseEntity<?> findAllBookRecord(Pageable pageable, @RequestParam(name = "page") int page)
    {
        pageable = PageRequest.of(page-1,1);
        return ResponseEntity.status(200).body(bookService.findAll(pageable));
    }

    @Operation(summary = "Find all books in database")
    @GetMapping(path = "/find-all-books")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findAllBooks(){
        return ResponseEntity.status(200).body(bookService.findAllBooks());
    }

    @Operation(summary = "Find books by book's name")
    @GetMapping(path = "/find-by-book-name")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByBookName(@RequestBody BodyRequest bodyRequest,
                                            Pageable pageable,
                                            @RequestParam(name = "page")int page)
    {
        pageable =PageRequest.of(page-1,1);
        String bookName = null;
        if(bodyRequest.getBody() instanceof String);
            bookName = (String) bodyRequest.getBody();
        return ResponseEntity.status(200).body(bookService.findByBookName(bookName,pageable));
    }

    @Operation(summary = "Find a book by id")
    @GetMapping(path = "/find-by-id")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByBookId(@RequestParam(name = "book_id") long bookId)
    {
        return bookService.findByBookId(bookId).isPresent()
                ?ResponseEntity.ok(bookService.findByBookId(bookId))
                :ResponseEntity.status(200).body(new MessageResponse("Book not found","fail"));
    }

    @Operation(summary = "Check a book if it available for borrowing ")
    @GetMapping(path = "/available/{book_id}")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> checkBookAvailable(@PathVariable(name = "book_id") long bookId)
    {
        return bookService.findByBookId(bookId).isPresent() && bookService.findByBookId(bookId).get().isAvailable()==true
                ?ResponseEntity.status(200).body(new MessageResponse("Available","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Not available","success"));
    }

    @Operation(summary = "Find all books of a author")
    @GetMapping(path = "/find-by-author")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> findByBookAuthor(@RequestBody BodyRequest bodyRequest,
                                              Pageable pageable,
                                              @RequestParam(name = "page") int page)
    {
        pageable = PageRequest.of(page-1,2);
        String authorName = null;
        if(bodyRequest.getBody() instanceof String)
            authorName = (String)bodyRequest.getBody();
        return ResponseEntity.ok(bookService.findByAuthor(authorName,pageable));
    }

    @Operation(summary = "Add a book")
    @PostMapping(path = "/add-book")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> addBook(@RequestBody Book book,
                                 @RequestParam(name = "publisher_id") long publisherId,
                                 @RequestParam(name = "book_type_id") long bookTypeId,
                                 @RequestParam(name = "amount") int amount) throws CloneNotSupportedException
            // đưa tất cả đưa vào DTO , NewBookDTO = Book + params
    {
        Optional<Publisher> publisherOptional = publisherService.findById(publisherId);
        if(!publisherOptional.isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Publisher not found","fail"));
        for(int i=0; i<amount;i++)
        {
            Book newBook = (Book)book.clone();
            newBook.setPublisher(publisherOptional.get());
            if(!bookService.add(newBook).isPresent())
                return ResponseEntity.status(200).body(new MessageResponse("Add book fail","fail"));
        }
        return ResponseEntity.status(201).body(new MessageResponse("Add book sucessfully","success"));
    }


    /*
    * Provide some a book's infos and amount of records -> add the book's records to db
    * */
    @Operation(summary = "Add a book with a number of book's records")
    @PostMapping(path = "/add")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> add(@RequestBody NewBookDTO newBookDTO) throws CloneNotSupportedException
    {
        Optional<Publisher> publisherOptional = publisherService.findById(newBookDTO.getPublisherId());
        if(!publisherOptional.isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Publisher not found","fail"));
        Optional<BookType> bookTypeOp = bookTypeRepo.getAllBookType().stream().filter(bookType1 -> bookType1.getBookTypeId()== newBookDTO.getBookTypeId()).findFirst();
        if(!bookTypeOp.isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Book Type not found","fail"));
        for(int i = 0; i< newBookDTO.getAmount(); i++)
        {
            Book newBook = (Book) newBookDTO.getBook().clone();
            newBook.setPublisher(publisherOptional.get());
            newBook.setBookType(bookTypeOp.get());
            if(!bookService.add(newBook).isPresent())
                return ResponseEntity.status(200).body(new MessageResponse("Add book fail","fail"));
        }
        return ResponseEntity.status(201).body(new MessageResponse("Add book sucessfully","success"));
    }

    @Operation(summary = "Import books through a excel file")
    @PostMapping(path = "/import")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> importBook(@RequestParam(name = "excel_file")MultipartFile multipartFile)
    {
        if(!excelFileService.validateExcelFile(multipartFile))
            return ResponseEntity.status(200).body(new MessageResponse("File type error","fail"));
        excelFileService.importBookFromExcelFile(multipartFile);
        return ResponseEntity.status(201).body(new MessageResponse("Import books successfully","success"));
    }


    /*
    * New Update API for book
    * */
    @Operation(summary = "Update all records of a book or a book's record")
    @PutMapping(path = "/update")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> updateByBookCode(@RequestBody BookRecordDTO bookRecordDTO)
    {
        if(bookRecordDTO.getBookCode()!=null) {
            if (bookService.findByBookCode(bookRecordDTO.getBookCode()).isEmpty())
                return ResponseEntity.status(204).body(new MessageResponse("Book not found (byte bookcode)", "fail"));
            return bookService.updateAll(bookRecordDTO).isPresent()
                    ? ResponseEntity.status(201).body(new MessageResponse("Update book successfully", "success"))
                    : ResponseEntity.status(304).body(new MessageResponse("Update book fail", "fail"));
        }
        else if(bookRecordDTO.getBookId()!=null)
        {
            if(!bookService.findByBookId(bookRecordDTO.getBookId()).isPresent())
                return ResponseEntity.status(200).body(new MessageResponse("Book not found","fail"));
            return bookService.update(bookRecordDTO).isPresent()
                    ?ResponseEntity.status(201).body(new MessageResponse("Update book successfully","success"))
                    :ResponseEntity.status(200).body(new MessageResponse("Update book fail","fail"));
        }
        return ResponseEntity.status(200).body(new MessageResponse("Both BookId and BookCode are null","fail"));
    }


    @Operation(summary = "Update a book's record by id")
    @PutMapping(path = "/update-rec")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody BookRecordDTO bookRecordDTO)
    {
        if(!bookService.findByBookId(bookRecordDTO.getBookId()).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Book not found","fail"));
        return bookService.update(bookRecordDTO).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Update book successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Update book fail","fail"));
    }


    @Operation(summary = "Delete a book")
    @DeleteMapping(path = "/delete-by-bookcode")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> deleteByBookCode(@RequestParam(name = "book_code") String bookCode)
    {
        return bookService.deleteByBookCode(bookCode)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete book successfully ","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Delete book fail","fail"));
    }


    /*
    * Provide book's record id or a book's code -> delete a record or all records of a book
    * New Delete API for book
     */
    @Operation(summary = "Delete a book's record or all records of a book")
    @DeleteMapping(path = "/delete/{book_id}")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> deleteBook(@PathVariable(name = "book_id",required = false) Long bookId,
                                        @RequestParam(name = "book_code", required = false,defaultValue ="null") String bookCode)
    {
        if(bookCode!=null && !bookCode.equals("null"))
            return bookService.deleteByBookCode(bookCode)
                    ? ResponseEntity.status(200)
                    .body(new MessageResponse("Deleted the book with code: "+bookCode+" successfully","success"))
                    :ResponseEntity.status(200)
                    .body(new MessageResponse("Deleted the book with code: "+bookCode+" fail","fail"));
        else if(bookId!=null)
            return bookService.deleteById(bookId)
                    ? ResponseEntity.status(200)
                    .body(new MessageResponse("Deleted the book's record with Id: "+bookId.longValue()+" successfully","success"))
                    : ResponseEntity.status(200)
                    .body(new MessageResponse("Deleted the book's record with Id: "+bookId.longValue()+" fail","fail"));
        else return ResponseEntity.status(200).body(new MessageResponse("Not found the book's record or the book","fail"));
    }
}
