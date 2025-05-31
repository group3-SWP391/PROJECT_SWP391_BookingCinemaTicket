package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT SUM(b.totalPrice) FROM Bill b")
    Double sumTotalPrice();

    @Query("SELECT COUNT(b) FROM Bill b")
    long countTicketsSold();
}

