package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.entities.BorrowFormDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BorrowFormDetailService {

    Optional<BorrowFormDetail> findById(long id);

    List<BorrowFormDetail> findByBook(long bookId);

    List<BorrowFormDetail> findByBorrowForm(long borrowFormId);

    Optional<BorrowFormDetail> add(BorrowFormDetail borrowFormDetail);

    boolean delete(long id);
}
