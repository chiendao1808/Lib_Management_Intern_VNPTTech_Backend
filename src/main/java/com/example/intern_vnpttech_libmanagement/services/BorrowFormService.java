package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.info.BorrowInfo;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BorrowFormDTO;
import com.example.intern_vnpttech_libmanagement.entities.BorrowForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BorrowFormService {

    Optional<BorrowForm> findByBorrowFormId(long borrowForm);

    List<BorrowForm> findByReader(long readerId);

    Optional<BorrowForm> add(BorrowForm borrowForm, BorrowInfo[] borrowInfos);

    Optional<BorrowForm> update(BorrowFormDTO borrowFormDTO);

    boolean delete(long borrowFormId);
}
