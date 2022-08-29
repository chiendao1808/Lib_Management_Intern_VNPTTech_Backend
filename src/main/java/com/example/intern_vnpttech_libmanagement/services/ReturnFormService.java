package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.info.ReturnInfo;
import com.example.intern_vnpttech_libmanagement.entities.ReturnForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReturnFormService {

    Optional<ReturnForm> findByReturnFormId(long returnFormId);

    List<ReturnForm> findByReader(long readerId);

    Optional<ReturnForm> add(ReturnForm returnForm, ReturnInfo[] returnInfos);

    Optional<ReturnForm> update(ReturnForm returnForm);

    boolean delete(long returnFormId);

}
