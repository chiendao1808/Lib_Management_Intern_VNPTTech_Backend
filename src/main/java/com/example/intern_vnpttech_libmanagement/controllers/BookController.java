package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.dto.request.BodyRequest;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BookRecordDTO;
import com.example.intern_vnpttech_libmanagement.dto.response.MessageResponse;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/libmng/api/book")
@Tag(name = "Book Controller")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PublisherService publisherService;

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
    @GetMapping(path = "/available")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> checkBookAvailable(@RequestParam(name = "book_id") long bookId)
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
    @PostMapping(path = "/add")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> add(@RequestBody Book book,
                                 @RequestParam(name = "publisher_id") long publisherId,
                                 @RequestParam(name = "book_type_id") long bookTypeId,
                                 @RequestParam(name = "amount") int amount) throws CloneNotSupportedException
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

    @Operation(summary = "Update all records of a book that have the same book's code")
    @PutMapping(path = "/update-all")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestParam(name = "book_code") String bookCode,
                                    @RequestBody BookRecordDTO bookRecordDTO)
    {
        if(bookService.findByBookCode(bookCode).isEmpty())
           return ResponseEntity.status(204).body(new MessageResponse("Book not found (byte bookcode)","fail"));
        else bookRecordDTO.setBookCode(bookCode);
        return bookService.updateAll(bookRecordDTO).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Update book successfully","success"))
                :ResponseEntity.status(304).body(new MessageResponse("Update book fail","fail"));
    }

    @Operation(summary = "Update a book by id")
    @PutMapping(path = "/update")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> update(@RequestParam(name = "book_id") long bookId,
                                    @RequestBody BookRecordDTO bookRecordDTO)
    {
        if(!bookService.findByBookId(bookId).isPresent())
            return ResponseEntity.status(200).body(new MessageResponse("Book not found","fail"));
        else bookRecordDTO.setBookId(bookId);
        return bookService.update(bookRecordDTO).isPresent()
                ?ResponseEntity.status(201).body(new MessageResponse("Update book successfully","success"))
                :ResponseEntity.status(200).body(new MessageResponse("Update book fail","fail"));
    }

    @Operation(summary = "Delete a book")
    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> deleteByBookCode(@RequestParam(name = "book_code") String bookCode)
    {
        return bookService.deleteByBookCode(bookCode)
                ?ResponseEntity.status(200).body(new MessageResponse("Delete book successfully ","success"))
                : ResponseEntity.status(200).body(new MessageResponse("Delete book fail","fail"));
    }
}
