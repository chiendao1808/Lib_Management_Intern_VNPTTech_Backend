package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "borrow_form_detail")
public class BorrowFormDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "borrow_form_id",nullable = false)
    @NotNull
    private BorrowForm borrowForm;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    @NotNull
    private Book book;

    @Column(name = "borrowed_book_state")
    private String borrowedBookState;

    @Column(name = "borrowed_at")
    private Timestamp borrowedAt;

    @Column(name = "return_deadline")
    private Timestamp returnDeadline;

    @Column(name = "deleted")
    private boolean deleted;

}
