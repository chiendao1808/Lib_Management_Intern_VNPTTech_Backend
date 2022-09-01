package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BookRecordDTO;
import com.example.intern_vnpttech_libmanagement.dto.info.ABook;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Page<Book> findByCriteria(long bookId, String bookName, String bookAuthor, String bookCode, long publisherId, boolean all,Pageable pageable);

    Page<Book> findAll(Pageable pageable);

    List<ABook> findAllBooks();

    Optional<Book> findByBookId(long bookId);

    Page<Book> findByBookName(String bookName,Pageable pageable);

    Page<Book> findByExactName(String bookName,Pageable pageable);

    Page<Book> findByAuthor(String authorName, Pageable pageable);

    List<Book> findByBookCode(String bookCode);

    Optional<Book> add(Book book);

    Optional<Book> updateAll(BookRecordDTO bookRecordDTO);

    Optional<Book> update(BookRecordDTO bookRecordDTO);

    boolean deleteById(long bookId);

    boolean deleteByBookCode(String bookCode);


}
