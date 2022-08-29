package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.ReaderDTO;
import com.example.intern_vnpttech_libmanagement.entities.Reader;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderRepo;
import com.example.intern_vnpttech_libmanagement.services.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderRepo readerRepo;

    @Override
    public Page<Reader> findAll(Pageable pageable) {
        try{
            return readerRepo.findAll(pageable);
        } catch (Exception ex)
        {
            log.error("Find all user error",ex);
            return Page.empty();
        }
    }

    @Override
    public Optional<Reader> findById(long readerId) {
        try{
            return readerRepo.findByReaderId(readerId);
        } catch (Exception ex)
        {
            log.error("Find reader by readerId error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reader> findByPhoneOrEmail(String readerPhone, String readerEmail) {
        try{
            return readerRepo.findByPhoneOrEmail(readerPhone,readerEmail);
        } catch (Exception ex)
        {
            log.error("Find reader by phone or email error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reader> add(Reader reader) {
        try{
            reader.setDeleted(false);
            reader.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            return Optional.ofNullable(readerRepo.save(reader));
        } catch (Exception ex)
        {
            log.error("Add reader error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reader> update(ReaderDTO readerDTO) {
        try{
            Reader readerToUpdate = readerRepo.findByReaderId(readerDTO.getReaderId()).get();
            readerToUpdate.setReaderName(readerDTO.getReaderName()!=null?readerDTO.getReaderName():readerToUpdate.getReaderName());
            readerToUpdate.setReaderEmail(readerDTO.getReaderEmail()!=null?readerDTO.getReaderEmail():readerToUpdate.getReaderEmail());
            readerToUpdate.setReaderPhone(readerDTO.getReaderPhone()!=null?readerDTO.getReaderPhone():readerToUpdate.getReaderPhone());
            readerToUpdate.setReaderAddress(readerDTO.getReaderAddress()!=null?readerDTO.getReaderAddress():readerToUpdate.getReaderAddress());
            return Optional.ofNullable(readerRepo.save(readerToUpdate));
        } catch (Exception ex)
        {
            log.error("Update reader error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long readerId) {
        try {
            return readerRepo.delete(readerId) > 0 ? true : false;
        } catch (Exception ex)
        {
            log.error("Delete reader error",ex);
            return false;
        }
    }
}
