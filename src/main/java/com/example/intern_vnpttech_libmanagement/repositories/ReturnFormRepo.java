package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.BorrowForm;
import com.example.intern_vnpttech_libmanagement.entities.ReturnForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnFormRepo extends JpaRepository<ReturnForm, Long> {

    @Query(value = "from ReturnForm rf where rf.returnFormId=?1 and rf.deleted=false")
    Optional<ReturnForm> findByReturnFormId(long returnFormId);

    @Query(value = "from ReturnForm rf where rf.reader.readerId=?1 and rf.deleted=false")
    List<ReturnForm> findByReader(long readerId);

    @Modifying
    @Transactional
    @Query(value = "update ReturnForm rf set rf.deleted = true where rf.returnFormId=?1 and rf.deleted=false ")
    int delete(long returnFormId);


}
