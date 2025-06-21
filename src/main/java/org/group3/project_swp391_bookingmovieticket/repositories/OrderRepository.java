package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.userId = :userId")
    List<Order> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId")
    long countByUserId(@Param("userId") Integer userId);
}