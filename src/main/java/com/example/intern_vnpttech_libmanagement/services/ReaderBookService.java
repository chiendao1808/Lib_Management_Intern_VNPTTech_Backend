package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReaderBookService {

    Optional<ReaderBook> findById(long id);

    List<ReaderBook> findByReader(long readerId);

    List<ReaderBook> findByBook(long bookId);

    Optional<ReaderBook> add(ReaderBook readerBook);

    boolean delete(long id);

    boolean deleteByBook(long bookId);


}
