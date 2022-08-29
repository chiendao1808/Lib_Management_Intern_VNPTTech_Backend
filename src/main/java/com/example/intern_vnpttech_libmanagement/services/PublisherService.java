package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.PublisherDTO;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PublisherService {

    Optional<Publisher> findById(long publisherId);

    Optional<Publisher> findByPhoneOrEmail(String publisherPhone, String publisherEmail);

    Optional<Publisher> findByFax(String publisherFax);

    Optional<Publisher> add(Publisher publisher);

    Optional<Publisher> update (PublisherDTO publisherDTO);

    boolean delete(long publisherId);

}
