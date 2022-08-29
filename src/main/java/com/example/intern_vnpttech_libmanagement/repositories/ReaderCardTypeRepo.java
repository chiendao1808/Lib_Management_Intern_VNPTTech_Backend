package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderCardTypeRepo extends JpaRepository<ReaderCardType,Long> {

    @Query(value = "from ReaderCardType cardType where cardType.cardTypeId=?1 and  cardType.deleted =false ")
    Optional<ReaderCardType> findById(long cardTypeId);

    @Query(value = "from ReaderCardType cardType where cardType.cardTypeName =?1 and  cardType.deleted =false")
    Optional<ReaderCardType> findByName(String cardTypeName);

    @Query(value = "select distinct cardType from ReaderCardType cardType where cardType.deleted=false")
    List<ReaderCardType> getAllCardType();

    @Modifying
    @Transactional
    @Query(value = "update ReaderCardType cardType set cardType.deleted = true where cardType.cardTypeId=?1 " +
            "and cardType.deleted=false ")
    int delete(long cardTypeId);

}
