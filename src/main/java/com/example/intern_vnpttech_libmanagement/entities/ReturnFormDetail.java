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
@Table(schema = DatabaseConstants.databaseName, name = "return_form_detail")
public class ReturnFormDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull
    private Book book ;

    @ManyToOne
    @JoinColumn(name = "return_form_id", nullable = false)
    @NotNull
    private ReturnForm returnForm;

    @Column(name = "returned_book_state")
    private String returnedBookState;

    @Column(name = "returned_at")
    private Timestamp returnedAt;

    @Column(name = "paid_amount")
    private Float paidAmount;

    @Column(name = "is_due")
    private boolean isDue;

    @Column(name = "deleted")
    private boolean deleted ;

}
