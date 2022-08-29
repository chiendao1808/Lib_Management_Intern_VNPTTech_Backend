package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.info.BorrowInfo;
import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BorrowFormDTO;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.entities.BorrowForm;
import com.example.intern_vnpttech_libmanagement.entities.BorrowFormDetail;
import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.BorrowFormDetailRepo;
import com.example.intern_vnpttech_libmanagement.repositories.BorrowFormRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderBookRepo;
import com.example.intern_vnpttech_libmanagement.services.BorrowFormService;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BorrowFormServiceImpl implements BorrowFormService {

    @Autowired
    private BorrowFormRepo borrowFormRepo;

    @Autowired
    private BorrowFormDetailRepo borrowFormDetailRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ReaderBookRepo readerBookRepo;

    @Autowired
    private ReaderCardRepo readerCardRepo;

    @Override
    public Optional<BorrowForm> findByBorrowFormId(long borrowFormId) {
        try {
            return borrowFormRepo.findByBorrowFormId(borrowFormId);
        }catch (Exception ex)
        {
            log.error("Find borrow form by id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public List<BorrowForm> findByReader(long readerId) {
        try{
            return borrowFormRepo.findByReader(readerId);
        } catch (Exception ex)
        {
            log.error("Find borrow form by Reader error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<BorrowForm> add(BorrowForm borrowForm, BorrowInfo[] borrowInfos) {
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            borrowForm.setBorrowedAt(now);
            if(borrowFormRepo.save(borrowForm)==null)
                throw new RuntimeException("Add borrow form error");
            int borrowingBook = readerBookRepo.countBorrowingBook(borrowForm.getReader().getReaderId());
            int borrowLimit = readerCardRepo.findByReader(borrowForm.getReader().getReaderId()).get().getCardType().getBorrowLimit();
            if(borrowingBook+borrowInfos.length>borrowLimit)
                throw new RuntimeException("Exceed borrowing limit, the maximum for this time is "+(borrowLimit-borrowingBook)+" books");
        for(BorrowInfo borrowInfo: borrowInfos)
        {
            BorrowFormDetail borrowFormDetail = new BorrowFormDetail();
            ReaderBook newReaderBook = new ReaderBook();
//            if(bookRepo.getAvailableBooks(borrowInfo.getBookCode()).isEmpty())
//            {
//                log.info("This book now is no available");
//                continue;
//            }
//
//            Book borrowedBook = bookRepo.getAvailableBooks(borrowInfo.getBookCode()).get(0);
            if(!bookRepo.findByBookId(borrowInfo.getBookId()).isPresent()) {
                log.info("Not found the book with id"+borrowInfo.getBookId());
                continue;
            }
            Book borrowedBook = bookRepo.findByBookId(borrowInfo.getBookId()).get();
            // set borrow form detais
            borrowFormDetail.setBook(borrowedBook);
            borrowFormDetail.setBorrowedBookState(borrowedBook.getBookState());
            borrowFormDetail.setDeleted(false);
            borrowFormDetail.setBorrowedAt(now);
            borrowFormDetail.setReturnDeadline(new Timestamp(now.getTime()+borrowInfo.getBorrowDays()*24*60*60*1000));
            borrowFormDetail.setBorrowForm(borrowForm);
            Optional<BorrowFormDetail> addedBRFD = Optional.ofNullable(borrowFormDetailRepo.save(borrowFormDetail));
            if(!addedBRFD.isPresent())
                throw new RuntimeException("Add BRFD error");
            // set reader-book
            newReaderBook.setBook(borrowedBook);
            newReaderBook.setReader(borrowForm.getReader());
            newReaderBook.setBorrowedAt(now);
            newReaderBook.setDeleted(false);
            newReaderBook.setReturnDeadline(borrowFormDetail.getReturnDeadline());
            Optional<ReaderBook> readerBookOptional = Optional.ofNullable(readerBookRepo.save(newReaderBook));
            if(!readerBookOptional.isPresent())
                throw new RuntimeException("Add ReaderBook error");
            bookRepo.setAvailable(false,borrowedBook.getBookId());
        }
        return Optional.of(borrowForm);
        } catch (Exception ex){
            log.error("Add Borrow Form error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<BorrowForm> update(BorrowFormDTO borrowFormDTO) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long borrowFormId) {
        try{
            return borrowFormRepo.delete(borrowFormId)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete borrow form detail fail");
            return false;
        }
    }
}
