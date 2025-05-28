package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT SUM(b.price) FROM Bill b")
    double sumTotalPrice();

    @Query("SELECT COUNT(t) FROM Ticket t")
    long countTicketsSold();
}
