package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.entities.BorrowFormDetail;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.BorrowFormDetailRepo;
import com.example.intern_vnpttech_libmanagement.services.BorrowFormDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BorrowFormDetailServiceImpl implements BorrowFormDetailService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private BorrowFormDetailRepo borrowFormDetailRepo;

    @Override
    public Optional<BorrowFormDetail> findById(long id) {
        try{
            return borrowFormDetailRepo.findById(id);
        } catch (Exception ex)
        {
            log.error("Find borrow form detail error by id",ex);
            return Optional.empty();
        }
    }

    @Override
    public List<BorrowFormDetail> findByBook(long bookId) {
        try{
            return borrowFormDetailRepo.findByBook(bookId);
        } catch (Exception ex)
        {
            log.error("Find borrow form detail by book error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public List<BorrowFormDetail> findByBorrowForm(long borrowFormId) {
        try{
            return borrowFormDetailRepo.findByBorrowForm(borrowFormId);
        } catch (Exception ex)
        {
            log.error("Find Borrow form detail by borrow form");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<BorrowFormDetail> add(BorrowFormDetail borrowFormDetail) {
        try{
            borrowFormDetail.setBorrowedAt(borrowFormDetail.getBorrowForm().getBorrowedAt());
            return Optional.ofNullable(borrowFormDetailRepo.save(borrowFormDetail));
        } catch (Exception ex)
        {
            log.error("add borrow form detail error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long id) {
        try{
            return borrowFormDetailRepo.delete(id)>0?true:false;
        } catch (Exception ex)
        {
            log.error("delete borrow form detail error",ex);
            return false;
        }
    }
}
