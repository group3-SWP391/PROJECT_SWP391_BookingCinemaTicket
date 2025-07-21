package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPaymentLinkRepository extends JpaRepository<PaymentLink, Integer> {
    List<PaymentLink> findAllByStatus(String status);

    PaymentLink findByOrderCode(long orderCode);

    boolean existsByOrderCodeAndStatus(long orderCode, String status);

    @Query("SELECT COUNT(p) > 0 FROM PaymentLink p WHERE p.schedule.id = :scheduleId " +
            "AND p.seatList LIKE CONCAT('%', :seatId, '%') " +
            "AND (p.status = 'PENDING' OR p.status = 'PAID')")
    boolean existsBySchedule_IdAndSeatListAndStatus(@Param("scheduleId") int scheduleId, @Param("seatId") String seatId);


}
