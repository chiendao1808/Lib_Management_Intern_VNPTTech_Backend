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
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
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
    private BookTypeRepo bookTypeRepo; // thay tạm cho service

    // Gộp tất cả api get thành 1 API getAll : truyền tất cả các params cần thiết

    /*
    * Filter API for book
    */
    @Operation(summary = "Get books with a criteria")
    @GetMapping()
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> get(@RequestParam(name = "book_id",required = false,defaultValue = "-1")Long bookId,
                                 @RequestParam(name = "book_name",required = false,defaultValue = "ALL") String bookName,
                                 @RequestParam(name = "book_author",required = false,defaultValue = "ALL") String bookAuthor,
                                 @RequestParam(name = "book_code",required = false,defaultValue = "NONE")String bookCode,
                                 @RequestParam(name = "publisher_id",required = false,defaultValue = "-1") Long publisherId,
                                 @RequestParam(name = "page",defaultValue = "1") Integer page,
                                 @RequestParam(name = "size", defaultValue = "1") Integer size,
                                 Pageable pageable,
                                 HttpServletRequest request)
    {
        pageable =PageRequest.of(page-1,size);
        return ResponseEntity.ok(bookService.findByCriteria(bookId,bookName,bookAuthor,bookCode,publisherId,pageable));
    }

    /*
    * Provide some a book's infos and amount of records -> add the book's records to db
    * */
    @Operation(summary = "Add a book with a number of book's records")
    @PostMapping()
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
    @PutMapping()
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestBody BookRecordDTO bookRecordDTO)
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


    /*
    * Provide book's record id or a book's code -> delete a record or all records of a book
    * New Delete API for book
     */
    @Operation(summary = "Delete a book's record or all records of a book")
    @DeleteMapping(path = "/{book_id}")
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
