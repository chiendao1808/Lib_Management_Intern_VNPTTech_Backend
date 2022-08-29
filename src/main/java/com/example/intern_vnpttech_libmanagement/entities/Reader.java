package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import com.sun.istack.NotNull;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "reader")
public class Reader implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id", nullable = false)
    private long readerId;

    @Column(name = "reader_name", nullable = false)
    @NotNull
    private String readerName;

    @Column(name = "reader_phone")
    private String readerPhone;

    @Column(name = "reader_email")
    private String readerEmail;

    @Column(name = "reader_address")
    private String readerAddress;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "deleted")
    private boolean deleted;

}
