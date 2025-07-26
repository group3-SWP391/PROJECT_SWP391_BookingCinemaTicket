package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface IPaymentLinkRepository extends JpaRepository<PaymentLink, Integer> {
    List<PaymentLink> findAllByStatus(String status);

    PaymentLink findByOrderCode(long orderCode);
    // Tìm đơn hàng theo user id
    @Query("SELECT o FROM PaymentLink o WHERE o.user.id = :userId")
    List<PaymentLink> findByUserId(@Param("userId") Integer userId);

    // Đếm đơn hàng theo user id
    @Query("SELECT COUNT(o) FROM PaymentLink o WHERE o.user.id = :userId")
    long countByUserId(@Param("userId") Integer userId);

    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END " +
            "FROM PaymentLink pl JOIN pl.tickets t " +
            "WHERE pl.user.id = :userId " +
            "AND pl.schedule.movie.name = :movieTitle " +
            "AND pl.createdAt < :before " +
            "AND t.status = true")
    boolean existsByUserIdAndMovieTitleAndTransactionDateBefore(
            @Param("userId") Integer userId,
            @Param("movieTitle") String movieTitle,
            @Param("before") LocalDateTime before);


    boolean existsByOrderCodeAndStatus(long orderCode, String status);

    @Query("SELECT COUNT(p) > 0 FROM PaymentLink p WHERE p.schedule.id = :scheduleId " +
            "AND p.seatList LIKE CONCAT('%', :seatId, '%') " +
            "AND (p.status = 'PENDING' OR p.status = 'PAID')")
    boolean existsBySchedule_IdAndSeatListAndStatus(@Param("scheduleId") int scheduleId, @Param("seatId") String seatId);

    boolean existsByUser_IdAndSchedule_Movie_Name(Integer userId, String movieName);

    List<PaymentLink> findByUserIdAndStatus(Integer userId, String status);

    boolean existsByUser_Id(Integer userId);

}
