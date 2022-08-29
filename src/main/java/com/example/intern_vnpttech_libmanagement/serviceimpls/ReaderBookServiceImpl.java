package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderBookRepo;
import com.example.intern_vnpttech_libmanagement.services.ReaderBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReaderBookServiceImpl implements ReaderBookService {

    @Autowired
    private ReaderBookRepo readerBookRepo;


    @Override
    public Optional<ReaderBook> findById(long id) {
        try{
            return readerBookRepo.findById(id);
        } catch (Exception ex)
        {
            log.error("Find ReaderBook By Id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public List<ReaderBook> findByReader(long readerId) {
        try{
            return readerBookRepo.findByReader(readerId);
        } catch (Exception ex)
        {
            log.error("Find ReaderBook by Reader error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReaderBook> findByBook(long bookId) {
        try{
            return readerBookRepo.findByBook(bookId);
        } catch (Exception ex)
        {
            log.error("Find ReaderBook by Book");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ReaderBook> add(ReaderBook readerBook) {
        try{
            return Optional.ofNullable(readerBookRepo.save(readerBook));
        } catch (Exception ex)
        {
            log.error("Add ReaderBook error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long id) {
        try{
            return readerBookRepo.delete(id)>0?true:false;
        }catch (Exception ex)
        {
            log.error("Delete ReaderBook by Id error",ex);
            return false;
        }
    }

    @Override
    public boolean deleteByBook(long bookId) {
        try{
            return readerBookRepo.deleteByBook(bookId)>0?true:false;
        }catch (Exception ex)
        {
            log.error("Delete ReaderBook By Book error",ex);
            return false;
        }
    }
}
