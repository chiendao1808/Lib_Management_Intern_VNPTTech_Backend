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
@Table(schema = DatabaseConstants.databaseName, name = "reader_book")
public class ReaderBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "reader_id",nullable = false)
    @NotNull
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    @NotNull
    private Book book;

    @Column(name = "borrowed_at",nullable = false)
    private Timestamp borrowedAt;

    @Column(name = "returned_at")
    private  Timestamp returnedAt;

    @Column(name = "return_deadline")
    private Timestamp returnDeadline;

    @Column(name = "deleted")
    private boolean deleted;


}
