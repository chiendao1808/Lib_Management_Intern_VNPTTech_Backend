package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepo extends JpaRepository<Staff,Long> {


    @Query(value = "select * from staff st where \n" +
            "(:staffId =-1 or st.staff_id = :staffId) \n " +
            "and (:staffUsername ='ALL' or st.staff_username = :staffUsername) \n " +
            "and (:staffEmail ='ALL' or st.staff_email = :staffEmail) \n " +
            "and (:staffPhone ='ALL' or st.staff_phone = :staffPhone) \n " +
            "and (:staffName ='ALL' or lower (st.staff_name) like concat('%',:staffName,'%')) \n " +
            "and st.deleted =false order by st.staff_id asc",nativeQuery = true)
    Page<Staff> findByCreterias(Long staffId,
                                String staffUsername,
                                String staffEmail,
                                String staffName,
                                String staffPhone,
                                Pageable pageable);

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
