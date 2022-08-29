package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.CurrencyType;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "reader_card_type")
public class ReaderCardType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardTypeId;

    @Column(name = "card_type_name" )
    private String cardTypeName;

    @Column(name = "borrow_limit")
    private int borrowLimit;

    @Column(name = "register_fee")
    private Float registerFee;

    @Column(name = "deleted")
    private boolean deleted;
}
