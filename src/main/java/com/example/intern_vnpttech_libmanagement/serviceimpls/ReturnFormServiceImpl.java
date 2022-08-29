package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.info.ReturnInfo;
import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import com.example.intern_vnpttech_libmanagement.entities.ReturnForm;
import com.example.intern_vnpttech_libmanagement.entities.ReturnFormDetail;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderBookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReturnFormDetailRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReturnFormRepo;
import com.example.intern_vnpttech_libmanagement.services.ReturnFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReturnFormServiceImpl implements ReturnFormService {

    @Autowired
    private ReturnFormRepo returnFormRepo;

    @Autowired
    private ReaderBookRepo readerBookRepo;

    @Autowired
    private ReturnFormDetailRepo returnFormDetailRepo;

    @Autowired
    private BookRepo bookRepo;

    @Override
    public Optional<ReturnForm> findByReturnFormId(long returnFormId) {
        try {
            return returnFormRepo.findByReturnFormId(returnFormId);
        }catch (Exception ex)
        {
            log.error("Find return form by return form id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public List<ReturnForm> findByReader(long readerId) {
        try{
            return returnFormRepo.findByReader(readerId);
        }catch (Exception ex)
        {
            log.error("Find return form by reader error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ReturnForm> add(ReturnForm returnForm, ReturnInfo[] returnInfos) { // phải code
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            returnForm.setReturnedAt(now);
            float totalPaid = 0;
            if(returnFormRepo.saveAndFlush(returnForm)==null)
                return Optional.empty();
            for(ReturnInfo returnInfo: returnInfos)
            {
                ReturnFormDetail returnFormDetail = new ReturnFormDetail();
                Optional<ReaderBook> readerBookOptional = readerBookRepo.findByNotReturnBook(returnInfo.getReturnBookId(),
                                                                            returnForm.getReader().getReaderId());
                if(!readerBookOptional.isPresent()) {
                    log.info("This reader didn't borrow this book or returned");
                    continue;
                }
                ReaderBook readerBook =readerBookOptional.get();
                returnFormDetail.setPaidAmount(returnInfo.getPaidAmount());
                returnFormDetail.setReturnForm(returnForm);
                returnFormDetail.setReturnedAt(returnForm.getReturnedAt());
                returnFormDetail.setBook(bookRepo.findByBookId(returnInfo.getReturnBookId()).get());
                returnFormDetail.setReturnedBookState(returnInfo.getReturnedBookState());
                returnFormDetail.setDue(now.after(readerBook.getReturnDeadline())?true:false);
                if(returnFormDetailRepo.save(returnFormDetail)==null)
                    throw new RuntimeException("Add return form detail error");
                readerBook.setReturnedAt(now);
                if(readerBookRepo.save(readerBook)==null)
                    throw new RuntimeException("Update reader book error");
                bookRepo.setAvailable(true,returnInfo.getReturnBookId());
                totalPaid+=returnInfo.getPaidAmount();
            }
            returnForm.setTotalPaidAmount(totalPaid);
            return Optional.ofNullable(returnFormRepo.saveAndFlush(returnForm));
        } catch (Exception ex)
        {
            log.error("Add return form error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReturnForm> update(ReturnForm returnForm) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long returnFormId) {
        try{
            return returnFormRepo.delete(returnFormId)>0?true:false;
        } catch (Exception ex)
        {
            log.error("delete return form error",ex);
            return false;
        }
    }
}
