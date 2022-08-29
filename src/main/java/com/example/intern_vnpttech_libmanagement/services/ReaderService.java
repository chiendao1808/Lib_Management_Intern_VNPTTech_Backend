package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderDTO;
import com.example.intern_vnpttech_libmanagement.entities.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ReaderService {

    Page<Reader> findAll(Pageable pageable);

    Optional<Reader> findById(long readerId);

    Optional<Reader> findByPhoneOrEmail(String readerPhone, String readerEmail);

    Optional<Reader> add(Reader reader);

    Optional<Reader> update(ReaderDTO readerDTO);

    boolean delete(long readerId);

}
