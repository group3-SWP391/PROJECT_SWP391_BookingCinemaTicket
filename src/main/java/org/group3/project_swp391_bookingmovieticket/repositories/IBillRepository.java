package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT SUM(b.price) FROM Bill b")
    Double sumTotalPrice(); // sửa double -> Double

    @Query("SELECT COUNT(t) FROM Ticket t")
    long countTicketsSold();

    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Bill b WHERE MONTH(b.createdTime) = :month AND YEAR(b.createdTime) = :year")
    Double getRevenueForMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Bill b " +
           "JOIN Ticket t ON b.id = t.bill.id " +
           "JOIN Schedule s ON t.schedule.id = s.id " +
           "WHERE s.movie.id = :movieId")
    Double getRevenueByMovie(@Param("movieId") int movieId);

    @Query("SELECT COUNT(t) FROM Ticket t " +
           "JOIN Schedule s ON t.schedule.id = s.id " +
           "WHERE s.movie.id = :movieId")
    Long getTicketsSoldByMovie(@Param("movieId") int movieId);

    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Bill b " +
           "JOIN Ticket t ON b.id = t.bill.id " +
           "JOIN Schedule s ON t.schedule.id = s.id " +
           "WHERE s.movie.id = :movieId " +
           "AND MONTH(b.createdTime) = :month " +
           "AND YEAR(b.createdTime) = :year")
    Double getMonthlyRevenueByMovie(@Param("movieId") int movieId, 
                                   @Param("month") int month, 
                                   @Param("year") int year);

}
