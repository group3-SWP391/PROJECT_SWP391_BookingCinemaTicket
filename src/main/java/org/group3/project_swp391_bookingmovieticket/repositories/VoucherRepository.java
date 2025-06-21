package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("SELECT v FROM Voucher v WHERE v.userId = :userId AND v.endDate > :currentDateTime AND v.startDate <= :currentDateTime")
    List<Voucher> findValidVouchersByUserId(@Param("userId") Integer userId, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT COUNT(v) FROM Voucher v WHERE v.userId = :userId")
    long countByUserId(@Param("userId") Integer userId);
}