package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private long publisherId;

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "publisher_phone")
    private String publisherPhone;

    @Column(name = "publisher_email")
    private String publisherEmail;

    @Column(name = "publisher_fax")
    private String publisherFax;

    @Column(name = "publisher")
    private String publisherAddress;

    @Column(name = "deleted")
    private boolean deleted;
}
