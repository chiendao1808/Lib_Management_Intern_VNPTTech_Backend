package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepo extends JpaRepository<Publisher,Long> {

    @Query(value = "from Publisher publisher where publisher.publisherId=?1 and publisher.deleted=false")
    Optional<Publisher> findByPublisherId(long publisherId);

    @Query(value = "from Publisher publisher where publisher.deleted=false")
    List<Publisher> findAll(); // find all publisher those have deleted = false

    @Query(value = "from Publisher publisher")
    List<Publisher> findAllExistting();

    @Query(value = "from Publisher publisher where (publisher.publisherPhone=?1 or publisher.publisherEmail=?2) " +
            "and publisher.deleted =false ")
    Optional<Publisher> findByPhoneOrEmail(String publisherPhone, String publisherEmail);

    @Query(value = "from Publisher publisher where publisher.publisherFax=?1 and publisher.deleted =false ")
    Optional<Publisher> findByFax(String publisherFax);

    @Modifying
    @Transactional
    @Query(value = "update Publisher publisher set publisher.deleted=true where publisher.publisherId=?1 and" +
            " publisher.deleted=false")
    int delete(long publisherId);
}
