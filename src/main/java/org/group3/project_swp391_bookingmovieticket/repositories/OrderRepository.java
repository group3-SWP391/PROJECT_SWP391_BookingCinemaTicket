package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Tìm đơn hàng theo user id
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Integer userId);

    // Đếm đơn hàng theo user id
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countByUserId(@Param("userId") Integer userId);



    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o WHERE o.user.id = :userId AND o.movieName = :movieTitle AND o.transactionDate < :before")
    boolean existsByUserIdAndMovieTitleAndTransactionDateBefore(@Param("userId") Integer userId,
                                                                @Param("movieTitle") String movieTitle,
                                                                @Param("before") LocalDateTime before);


    boolean existsByUser_IdAndMovieName(Integer userId, String movieName);

    List<Order> findByUserIdAndStatus(Integer userId, String status);


}
