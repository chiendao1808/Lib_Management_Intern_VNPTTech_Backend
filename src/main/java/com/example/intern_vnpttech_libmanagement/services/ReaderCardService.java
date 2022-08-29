package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderCardDTO;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ReaderCardService{

    Optional<ReaderCard> findByCardId(long cardId);

    Optional<ReaderCard> findByReader(long readerId);

    Optional<ReaderCard> publish(ReaderCard readerCard);

    Optional<ReaderCard> update(ReaderCardDTO readerCardDTO);

    boolean delete(long cardId);

    boolean deleteByReader(long readerId);

}
