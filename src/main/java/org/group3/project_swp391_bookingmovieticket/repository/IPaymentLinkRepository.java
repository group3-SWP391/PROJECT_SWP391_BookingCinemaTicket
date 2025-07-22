package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPaymentLinkRepository extends JpaRepository<PaymentLink, Integer> {

    // Tìm đơn hàng theo user id
    @Query("SELECT o FROM PaymentLink o WHERE o.user.id = :userId")
    List<PaymentLink> findByUserId(@Param("userId") Integer userId);

    // Đếm đơn hàng theo user id
    @Query("SELECT COUNT(o) FROM PaymentLink o WHERE o.user.id = :userId")
    long countByUserId(@Param("userId") Integer userId);

    @Query("SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END " +
            "FROM PaymentLink pl JOIN pl.tickets t " +
            "WHERE pl.user.id = :userId " +
            "AND pl.movieName = :movieTitle " +
            "AND pl.transactionDate < :before " +
            "AND t.status = true")
    boolean existsByUserIdAndMovieTitleAndTransactionDateBefore(
            @Param("userId") Integer userId,
            @Param("movieTitle") String movieTitle,
            @Param("before") LocalDateTime before);



    boolean existsByUser_IdAndMovieName(Integer userId, String movieName);

    List<PaymentLink> findByUserIdAndStatus(Integer userId, String status);

    boolean existsByUser_Id(Integer userId);

}
