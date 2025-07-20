package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    @Query("SELECT v FROM Voucher v WHERE v.userId = :userId AND v.endDate > :currentDateTime AND v.startDate <= :currentDateTime")
    List<Voucher> findValidVouchersByUserId(@Param("userId") Integer userId, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT COUNT(v) FROM Voucher v WHERE v.userId = :userId")
    long countByUserId(@Param("userId") Integer userId);

    @Query("SELECT v FROM Voucher v WHERE (v.userId = :userId OR v.userId IS NULL) AND v.endDate > :currentDateTime AND v.startDate <= :currentDateTime AND v.isUsed = false AND v.currentUsageCount < v.maxUsageCount")
    List<Voucher> findValidVouchersByUserIdAndConditions(@Param("userId") Integer userId, @Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("SELECT v FROM Voucher v WHERE v.code = :code")
    Optional<Voucher> findByCode(@Param("code") String code);
}