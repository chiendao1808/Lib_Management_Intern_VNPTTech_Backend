package com.example.intern_vnpttech_libmanagement.dto.info;

import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;

import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// a book has many books (records)
public class ABook {

    private String bookCode;

    private String bookName;

    private int bookPublishYear;

    private String bookType;

    private String bookAuthor;

    private Publisher publisher;

    private  int amountAvailable;

    private boolean deleted;

    public ABook(Book book, int amountAvailable){
        this.bookCode =book.getBookCode();
        this.bookName=book.getBookName();
        this.bookAuthor =book.getBookAuthor();
        this.publisher =book.getPublisher();
        this.bookPublishYear =book.getBookPublishYear();
        this.bookType =book.getBookType();
        this.amountAvailable = amountAvailable;
        this.deleted = amountAvailable==0?true:false;
    }

}
