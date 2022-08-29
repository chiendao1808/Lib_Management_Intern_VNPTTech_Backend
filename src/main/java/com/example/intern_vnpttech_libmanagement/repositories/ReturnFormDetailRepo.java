package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.BorrowFormDetail;
import com.example.intern_vnpttech_libmanagement.entities.ReturnFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnFormDetailRepo extends JpaRepository<ReturnFormDetail,Long> {

    @Query(value = "from ReturnFormDetail rfd where rfd.id=?1 and rfd.deleted=false")
    Optional<ReturnFormDetail> findById(long id );

    @Query(value = "from ReturnFormDetail  rfd where rfd.returnForm.returnFormId=?1 and rfd.deleted =false")
    List<ReturnFormDetail> findByReturnForm(long returnFormId);

    @Query(value = "from ReturnFormDetail  rfd where rfd.book.bookId=?1 and rfd.deleted =false")
    List<ReturnFormDetail> findByBook(long bookId);

    @Modifying
    @Transactional
    @Query(value = "update ReturnFormDetail rfd set rfd.deleted=true where rfd.id=?1 and rfd.deleted=false")
    int delete(long id);
}
