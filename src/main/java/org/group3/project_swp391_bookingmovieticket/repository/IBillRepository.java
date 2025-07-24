package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findByUserId(int id);
    boolean existsByUserId(int id);

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

    @Query("SELECT new map(" +
           "branch.name as branchName, " +
           "room.name as roomName, " +
           "FORMAT(s.startTime, 'yyyy-MM-dd') as showDate, " +
           "FORMAT(s.startTime, 'HH:mm') as showTime, " +
           "COUNT(t.id) as ticketsSold, " +
           "COALESCE(SUM(b.price), 0) as revenue) " +
           "FROM Bill b " +
           "JOIN Ticket t ON b.id = t.bill.id " +
           "JOIN Schedule s ON t.schedule.id = s.id " +
           "JOIN Room room ON s.room.id = room.id " +
           "JOIN Branch branch ON room.branch.id = branch.id " +
           "WHERE s.movie.id = :movieId " +
           "GROUP BY branch.name, room.name, FORMAT(s.startTime, 'yyyy-MM-dd'), FORMAT(s.startTime, 'HH:mm') " +
           "ORDER BY FORMAT(s.startTime, 'yyyy-MM-dd') DESC, FORMAT(s.startTime, 'HH:mm') DESC")
    List<Map<String, Object>> getMovieRevenueBreakdownByMovieId(@Param("movieId") int movieId);

}
