package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.entities.ReturnFormDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReturnFormDetailService {

    Optional<ReturnFormDetail> findById(long id);

    List<ReturnFormDetail> findByReturnForm(long returnFormId);

    List<ReturnFormDetail> findByBook(long bookId);

    Optional<ReturnFormDetail> add(ReturnFormDetail returnFormDetail);

    boolean delete(long id);
}
