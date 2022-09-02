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

    @Query(value = "select * from publisher pub where pub.publisher_id = :publisherId and pub.deleted = false ",nativeQuery = true)
    Optional<Publisher> findByPublisherId(long publisherId);

    @Query(value = "select * from publisher pub where pub.deleted =false order by pub.publisher_id asc",nativeQuery = true)
    List<Publisher> findAll(); // find all publisher those have deleted = false

    @Query(value = "select * from publisher pub where pub.publisher_name = :publisherName and pub.deleted =false",nativeQuery = true)
    List<Publisher> findByPublisherName(String publisherName);

//    @Query(value = "select publisher from Publisher publisher where publisher.deleted =false ")
//    List<Publisher> findAllExistting();

    @Query(value = "select * from publisher pub where (pub.publisher_phone = :publisherPhone" +
            " or pub.publisher_email = :publisherEmail) and pub.deleted =false",nativeQuery = true)
    Optional<Publisher> findByPhoneOrEmail(String publisherPhone, String publisherEmail);

    @Query(value = "select * from publisher pub where pub.publisher_fax = :publisherFax and pub.deleted =false",nativeQuery = true)
    Optional<Publisher> findByFax(String publisherFax);

    @Query(value = "select count(*) from publisher pub where pub.publisher_name = :publisherName and pub.deleted =false",nativeQuery = true)
    int existedPublisher(String publisherName);

    @Modifying
    @Transactional
    @Query(value = "update publisher pub set pub.deleted =true where pub.publisher_id = :publisherId and pub.deleted =false", nativeQuery = true)
    int delete(long publisherId);
}
