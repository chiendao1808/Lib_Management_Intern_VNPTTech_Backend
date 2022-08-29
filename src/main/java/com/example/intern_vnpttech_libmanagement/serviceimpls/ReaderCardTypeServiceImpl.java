package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderCardTypeRepo;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReaderCardTypeServiceImpl implements ReaderCardTypeService {

    @Autowired
    private ReaderCardTypeRepo cardTypeRepo;

    @Override
    public Optional<ReaderCardType> findById(long cardTypeId) {
        try{
            return cardTypeRepo.findById(cardTypeId);
        } catch (Exception ex)
        {
            log.error("Find card type by Id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReaderCardType> findByName(String cardTypeName) {
        try{
            return cardTypeRepo.findByName(cardTypeName);
        } catch (Exception ex)
        {
            log.error("Find card type by name error",ex);
            return Optional.empty();
        }
    }
}
