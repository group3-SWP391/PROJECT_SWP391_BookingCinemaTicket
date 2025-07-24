package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findTicketsBySchedule_IdAndSeat_Id(Integer scheduleId, Integer seatId);

    List<Ticket> findTicketsBySchedule_Id(Integer scheduleId);
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.bill b " +
            "WHERE t.schedule.id = :scheduleId AND b.user.id <> :userId " +
            "ORDER BY t.id DESC")
    List<Ticket> findTicketsOfOtherUser(@Param("scheduleId") Integer scheduleId,
                                          @Param("userId") Integer userId);

    @Query("SELECT t FROM Ticket t " +
            "JOIN t.bill b " +
            "WHERE b.user.id = :userId AND t.schedule.id = :scheduleId " +
            "ORDER BY t.id DESC")
    List<Ticket> findTicketsOfCurrentUserAndScheduleId(@Param("userId") Integer userId,
                                                       @Param("scheduleId") Integer scheduleId);

    @Query("SELECT t FROM Ticket t " +
            "JOIN t.bill b " +
            "JOIN PaymentLink p ON p.bill.id = b.id " +
            "WHERE p.orderCode = :orderCode")
    List<Ticket> findTicketsByOrderCode(@Param("orderCode") long orderCode);
    @Query("SELECT t FROM Ticket t WHERE t.bill.user.id = :userId AND t.schedule.movie.name = :movieName")
    Ticket findByUserIdAndMovieName(@Param("userId") Integer userId, @Param("movieName") String movieName);

    List<Ticket> findByBill_Id(Integer billId);

    // Thêm method custom nếu cần, ví dụ:
    List<Ticket> findByBillId(Integer billId);
    List<Ticket> findByScheduleId(Integer scheduleId
}