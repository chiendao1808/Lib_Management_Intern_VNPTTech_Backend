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

    @Query(value = "select * from reader rd where rd.deleted =false ",nativeQuery = true)
    Page<Reader> findAll(Pageable pageable);

    @Query(value = "select * from reader rd where rd.reader_id =:readerId and rd.deleted = false",nativeQuery = true)
    Optional<Reader> findByReaderId(long readerId);

    @Query(value = "select * from reader rd where rd.reader_name like '%:readerName%' and rd.deleted =false",nativeQuery = true)
    Page<Reader> findByReaderName(String readerName,Pageable pageable);

    @Query(value = "select * from reader rd where (rd.reader_phone = :readerPhone or rd.reader_email = :readerEmail) " +
            "and rd.deleted =false",nativeQuery = true)
    Optional<Reader> findByPhoneOrEmail(String readerPhone, String readerEmail);

    @Modifying
    @Transactional
    @Query(value = "update reader rd set rd.deleted =true where rd.reader_id = :readerId and rd.deleted =false",nativeQuery = true)
    int delete(long readerId);
}
