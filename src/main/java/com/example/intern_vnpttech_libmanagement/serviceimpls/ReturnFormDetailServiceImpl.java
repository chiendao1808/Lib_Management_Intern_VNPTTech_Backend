package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.entities.ReturnFormDetail;
import com.example.intern_vnpttech_libmanagement.repositories.ReturnFormDetailRepo;
import com.example.intern_vnpttech_libmanagement.services.ReturnFormDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReturnFormDetailServiceImpl implements ReturnFormDetailService {

    @Autowired
    private ReturnFormDetailRepo returnFormDetailRepo;

    @Override
    public Optional<ReturnFormDetail> findById(long id) {
      try{
          return returnFormDetailRepo.findById(id);
      }catch (Exception ex)
      {
          log.error("Find return form detail by id error",ex);
          return Optional.empty();
      }
    }

    @Override
    public List<ReturnFormDetail> findByReturnForm(long returnFormId) {
        try {
            return returnFormDetailRepo.findByReturnForm(returnFormId);
        }catch (Exception ex)
        {
            log.error("Find return form details by returnForm error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReturnFormDetail> findByBook(long bookId) {
        try {
            return returnFormDetailRepo.findByBook(bookId);
        }catch (Exception ex)
        {
            log.error("Find return form details by book error",ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ReturnFormDetail> add(ReturnFormDetail returnFormDetail) {
        try{
            returnFormDetail.setReturnedAt(returnFormDetail.getReturnForm().getReturnedAt());
            return Optional.ofNullable(returnFormDetailRepo.save(returnFormDetail));
        }catch (Exception ex)
        {
            log.error("Add return form detail error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long id) {
        try{
            return returnFormDetailRepo.delete(id)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete return form detail error",ex);
            return false;
        }
    }
}
