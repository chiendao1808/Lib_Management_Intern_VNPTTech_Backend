package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DatabaseConstants.databaseName , name = "book_type")
public class BookType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_type_id")
    private long bookTypeId;

    @Column(name = "book_type_name",nullable = false)
    private String bookTypeName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted")
    private boolean deleted;


}
