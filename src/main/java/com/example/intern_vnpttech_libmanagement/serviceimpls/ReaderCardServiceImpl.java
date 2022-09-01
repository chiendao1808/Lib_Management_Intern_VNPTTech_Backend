package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderCardDTO;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderCardRepo;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class ReaderCardServiceImpl implements ReaderCardService {

    @Autowired
    private ReaderCardRepo readerCardRepo;

    public final long aMonth =30*24*60*60*1000L;

    public final long aYear = 365*24*60*60*1000L;

    @Override
    public Optional<ReaderCard> findByCardId(long cardId) {
        try{
            return readerCardRepo.findByCardId(cardId);
        } catch (Exception ex)
        {
            log.error("Find card by id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReaderCard> findByReader(long readerId) {
        try{
            return readerCardRepo.findByReader(readerId);
        } catch (Exception ex)
        {
            log.error("Find card by reader error",ex);
            return Optional.empty();
        }
    }

    // Publish a card for a reader
    @Override
    public Optional<ReaderCard> publish(ReaderCard readerCard) {
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            readerCard.setPublishedAt(now);
            readerCard.setExpiredAt(new Timestamp(now.getTime()+aYear));
            readerCard.setExpired(false);
            readerCard.setDeleted(false);
            return Optional.ofNullable(readerCardRepo.save(readerCard));
        } catch (Exception ex)
        {
            log.error("Publish a card error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReaderCard> update(ReaderCardDTO readerCardDTO) {
        try {
            ReaderCard cardToUpdate = readerCardRepo.findByCardId(readerCardDTO.getCardId()).get();
           // cardToUpdate.setCardBalance(cardToUpdate.getCardBalance()-readerCardDTO.getMinusAmount());
            if(readerCardDTO.getExtendMonths()!=null) // extend card;
            {
                long extendTime = readerCardDTO.getExtendMonths()==12?aYear:readerCardDTO.getExtendMonths()*aMonth;
                cardToUpdate.setExpiredAt(new Timestamp(cardToUpdate.getExpiredAt().getTime()+extendTime));
                cardToUpdate.setExpired(false);
            }
            return  Optional.ofNullable(readerCardRepo.save(cardToUpdate));
        } catch (Exception ex)
        {
            log.error("update a card error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long cardId) {
        try{
            return readerCardRepo.delete(cardId)>0?true:false;
        }catch (Exception ex)
        {
            log.error("Delete card by id error",ex);
            return false;
        }
    }

    @Override
    public boolean deleteByReader(long readerId) {
        try{
            return readerCardRepo.deleteByReader(readerId)>0?true:false;
        }catch (Exception ex)
        {
            log.error("Delete card by reader error",ex);
            return false;
        }
    }
}
