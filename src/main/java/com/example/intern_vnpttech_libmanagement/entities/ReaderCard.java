package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "reader_card")
public class ReaderCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false)
    private long cardId;

    @ManyToOne
    @JoinColumn(name = "reader_id",nullable = false)
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "card_type_id")
    private ReaderCardType cardType;

    @Column(name = "card_balance")
    private float cardBalance; // số dư thẻ -> chức năng thanh toán tự động nếu phát triển thêm

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "is_expired")
    private boolean expired;

    @Column(name = "deleted")
    private boolean deleted;
}
