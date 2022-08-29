package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.LibStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibStaffRepo extends JpaRepository<LibStaff,Long> {

    @Query(value = "from LibStaff staff where staff.deleted =false ")
    List<LibStaff> findAll();

    @Query(value = "from LibStaff staff where staff.staffId=?1 and staff.deleted=false")
    Optional<LibStaff> findById(long staffId);

    @Query("from LibStaff staff where (staff.staffUsername=?1 or staff.staffEmail=?2) " +
            " and staff.deleted =false ")
    Optional<LibStaff> findByUsernameOrEmail(String staffUsername,String staffEmail);

    @Query(value = "select count(staff) from LibStaff staff where " +
            "(staff.staffUsername=?1 or staff.staffEmail=?2 or staff.staffPhone =?3) and staff.deleted =false")
    int existsByStaffUsernameOrStaffEmailOrStaffPhone(String staffUserName,String staffEmail,String staffPhone);


    @Modifying
    @Transactional
    @Query(value = "update LibStaff  staff set staff.deleted=true where staff.staffId=?1 and staff.deleted=false")
    int delete(long staffId);
}
