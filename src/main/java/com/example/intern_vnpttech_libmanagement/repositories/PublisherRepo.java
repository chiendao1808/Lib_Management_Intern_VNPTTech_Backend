package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepo extends JpaRepository<Publisher,Long> {

    @Query(value = "from Publisher publisher where publisher.publisherId=?1 and publisher.deleted=false")
    Optional<Publisher> findByPublisherId(long publisherId);

    @Query(value = "from Publisher publisher where publisher.deleted=false")
    List<Publisher> findAll();
}
