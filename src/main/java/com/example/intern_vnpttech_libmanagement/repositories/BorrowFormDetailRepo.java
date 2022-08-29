package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.BorrowFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowFormDetailRepo extends JpaRepository<BorrowFormDetail,Long> {

    @Query(value = "from BorrowFormDetail  brfd where brfd.deleted =false ")
    Optional<BorrowFormDetail> findById(long id);

    @Query(value = "from BorrowFormDetail  brfd where brfd.borrowForm.borrowFormId=?1 and brfd.deleted=false")
    List<BorrowFormDetail> findByBorrowForm(long borrowFormId);

    @Query(value = "from BorrowFormDetail  brfd where brfd.book.bookId=?1 and brfd.deleted=false")
    List<BorrowFormDetail> findByBook(long bookId);


    @Query(value = "update BorrowFormDetail  brfd set brfd.deleted =true where brfd.id =?1 and brfd.deleted =false")
    int delete(long id);
}
