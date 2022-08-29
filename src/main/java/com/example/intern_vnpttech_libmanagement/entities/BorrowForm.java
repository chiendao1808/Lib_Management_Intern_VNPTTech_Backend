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
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DatabaseConstants.databaseName, name = "borrow_form")
public class BorrowForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_form_id",nullable = false)
    private long borrowFormId;

    @ManyToOne
    @JoinColumn(name = "reader_id",nullable = false)
    @NotNull
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "staff_id",nullable = false)
    private LibStaff doStaff;

//    @ManyToMany
//    @JoinTable(name = "borrow_form_detail",
//            joinColumns = @JoinColumn(name ="borrow_form_id"),
//            inverseJoinColumns = @JoinColumn(name = "book_id"))
//    private List<Book> borrowedBooks;

    @Column(name = "borrowed_at",nullable = false)
    private Timestamp borrowedAt;

    @Column(name = "deleted")
    private boolean deleted;

}
