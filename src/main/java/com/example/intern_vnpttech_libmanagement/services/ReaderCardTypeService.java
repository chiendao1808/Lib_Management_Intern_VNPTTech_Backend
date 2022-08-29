package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import org.springframework.stereotype.Service;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

@Service
public interface ReaderCardTypeService {

    Optional<ReaderCardType> findById(long cardTypeId);

    Optional<ReaderCardType> findByName(String cardTypeName);
}
