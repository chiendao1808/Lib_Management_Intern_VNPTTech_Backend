package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader,Long> {

    @Query(value = "from Reader reader where reader.deleted =false")
    Page<Reader> findAll(Pageable pageable);

    @Query(value = "from Reader reader where reader.readerId =?1 and reader.deleted =false")
    Optional<Reader> findByReaderId(long readerId);

    @Query(value = "from Reader reader where reader.readerName like concat('%',?1,'%') and reader.deleted =false ")
    Page<Reader> findByReaderName(String readerName,Pageable pageable);

    @Query(value = "from Reader reader where (reader.readerPhone=?1 or reader.readerEmail =?2) and reader.deleted=false")
    Optional<Reader> findByPhoneOrEmail(String readerPhone, String readerEmail);

    @Modifying
    @Transactional
    @Query("update Reader reader set reader.deleted =true where reader.readerId =?1 and reader.deleted =false")
    int delete(long readerId);
}
