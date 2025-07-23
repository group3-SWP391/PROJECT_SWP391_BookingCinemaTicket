package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Integer> {

    // Lấy các voucher đang hoạt động, chưa hết hạn, đã bắt đầu, chưa vượt giới hạn sử dụng
    @Query("SELECT v FROM Voucher v WHERE v.endDate > :currentDateTime AND v.startDate <= :currentDateTime AND v.isUsed = false AND v.currentUsageCount < v.maxUsageCount")
    List<Voucher> findValidVouchers(@org.springframework.data.repository.query.Param("currentDateTime") LocalDateTime currentDateTime);

    // Tìm theo mã code
    @Query("SELECT v FROM Voucher v WHERE v.code = :code")
    Optional<Voucher> findByCode(@org.springframework.data.repository.query.Param("code") String code);
}
