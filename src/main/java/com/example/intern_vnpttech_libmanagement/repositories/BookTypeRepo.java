package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BookTypeRepo extends JpaRepository<BookType,Long> {

    @Query(value = "select distinct bookType from BookType bookType where bookType.deleted =false")
    List<BookType> getAllBookType();


}
