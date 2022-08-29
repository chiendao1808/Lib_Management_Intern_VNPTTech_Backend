package com.example.intern_vnpttech_libmanagement.services;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ReaderCardRepo extends JpaRepository<ReaderCard,Long> {

    @Query(value = "from ReaderCard rc where rc.deleted =false")
    Optional<ReaderCard> findByCardId(long cardId);

    @Query(value = "from ReaderCard rc where rc.reader.readerId =?1 and rc.deleted =false")
    Optional<ReaderCard> findByReader(long readerId);

    @Modifying
    @Transactional
    @Query(value = "update ReaderCard rc set rc.deleted =true where rc.cardId=?1 and rc.deleted=false")
    int delete(long cardId);

    @Modifying
    @Transactional
    @Query(value = "update ReaderCard rc set rc.deleted =true where rc.reader.readerId=?1 and rc.deleted=false")
    int deleteByReader(long readerId);
}
