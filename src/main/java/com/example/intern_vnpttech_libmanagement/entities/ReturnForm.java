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
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = DatabaseConstants.databaseName, name = "return_form")
public class ReturnForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_form_id", nullable = false)
    private long returnFormId;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "staff_id",nullable = false)
    private Staff didStaff;

    @Column(name = "total_paid_amount")
    private Float totalPaidAmount;

    @Column(name = "returned_at")
    private Timestamp returnedAt;

    @Column(name = "deleted")
    private boolean deleted;
}
