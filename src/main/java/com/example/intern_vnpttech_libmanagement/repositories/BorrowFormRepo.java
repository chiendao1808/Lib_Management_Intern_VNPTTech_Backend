package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.BorrowForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowFormRepo extends JpaRepository<BorrowForm,Long> {

    @Query(value = "from BorrowForm brf where  brf.borrowFormId =?1 and brf.deleted =false")
    Optional<BorrowForm> findByBorrowFormId(long borrowFormId);

    @Query(value = "from BorrowForm brf where brf.reader.readerId =?1 and brf.deleted =false")
    List<BorrowForm> findByReader(long readerId);

    @Modifying
    @Transactional
    @Query(value = "update BorrowForm  brf set brf.deleted=true where brf.borrowFormId=?1 and brf.deleted =false")
    int delete(long borrowFormId);

}
