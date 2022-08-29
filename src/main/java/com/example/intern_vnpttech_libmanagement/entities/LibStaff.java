package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DatabaseConstants.databaseName,name = "lib_staff")
public class LibStaff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id",nullable = false)
    private long staffId;

    @Column(name = "staff_username",nullable = false)
    private String staffUsername;

    @Column(name = "staff_password")
    @JsonIgnore
    private String staffPassword;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_phone")
    private String staffPhone;

    @Column(name = "staff_email")
    private String staffEmail;

    @Column(name = "staff_address")
    private String staffAddress;

    @Column(name = "staff_role")
    private String staffRole;

    @Column(name = "deleted")
    private boolean deleted;
}
