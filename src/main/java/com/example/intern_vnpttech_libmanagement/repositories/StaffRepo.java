package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepo extends JpaRepository<Staff,Long> {

    @Query(value = "from Staff staff where staff.deleted =false ")
    List<Staff> findAll();

    @Query(value = "from Staff staff where staff.staffId=?1 and staff.deleted=false")
    Optional<Staff> findById(long staffId);

    @Query("from Staff staff where (staff.staffUsername=?1 or staff.staffEmail=?2) " +
            " and staff.deleted =false ")
    Optional<Staff> findByUsernameOrEmail(String staffUsername, String staffEmail);

    @Query(value = "select count(staff) from Staff staff where " +
            "(staff.staffUsername=?1 or staff.staffEmail=?2 or staff.staffPhone =?3) and staff.deleted =false")
    int existsByStaffUsernameOrStaffEmailOrStaffPhone(String staffUserName,String staffEmail,String staffPhone);


    @Modifying
    @Transactional
    @Query(value = "update Staff  staff set staff.deleted=true where staff.staffId=?1 and staff.deleted=false")
    int delete(long staffId);
}
