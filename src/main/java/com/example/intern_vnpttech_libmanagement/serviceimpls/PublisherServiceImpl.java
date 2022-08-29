package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.PublisherDTO;
import com.example.intern_vnpttech_libmanagement.entities.Publisher;
import com.example.intern_vnpttech_libmanagement.repositories.PublisherRepo;
import com.example.intern_vnpttech_libmanagement.services.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PublisherServiceImpl implements PublisherService {


    @Autowired
    private PublisherRepo publisherRepo;

    @Override
    public Optional<Publisher> findById(long publisherId) {
        try{
            return publisherRepo.findById(publisherId);
        } catch (Exception ex)
        {
            log.error("Find Publisher by Id error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Publisher> findByPhoneOrEmail(String publisherPhone, String publisherEmail) {
        try{
            return publisherRepo.findByPhoneOrEmail(publisherPhone!=null?publisherPhone:"",
                                                    publisherEmail!=null?publisherEmail:"");
        } catch (Exception ex)
        {
            log.error("Find Publisher by phone or email");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Publisher> findByFax(String publisherFax) {
        try{
            return publisherRepo.findByFax(publisherFax);
        } catch (Exception ex)
        {
            log.error("Find publisher by fax",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Publisher> add(Publisher publisher) {
        try{
            return Optional.ofNullable(publisherRepo.save(publisher));
        } catch (Exception ex)
        {
            log.error("Add publisher error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Publisher> update(PublisherDTO publisherDTO) {
        try{
            Publisher publisherToUpdate = publisherRepo.findByPublisherId(publisherDTO.getPublisherId()).get();
            publisherToUpdate.setPublisherPhone(publisherDTO.getPublisherPhone()!=null
                    ?publisherToUpdate.getPublisherPhone()
                    :publisherToUpdate.getPublisherPhone());
            publisherToUpdate.setPublisherEmail(publisherDTO.getPublisherEmail()!=null
                    ?publisherDTO.getPublisherEmail()
                    :publisherToUpdate.getPublisherEmail());
            publisherToUpdate.setPublisherFax(publisherDTO.getPublisherFax()!=null
                    ?publisherDTO.getPublisherFax()
                    :publisherToUpdate.getPublisherFax());
            // continue
            return Optional.ofNullable(publisherRepo.save(publisherToUpdate));
        } catch (Exception ex)
        {
            log.error("Update publisher error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(long publisherId) {
        try{
            return publisherRepo.delete(publisherId)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete publisher error",ex);
            return false;
        }
    }
}
