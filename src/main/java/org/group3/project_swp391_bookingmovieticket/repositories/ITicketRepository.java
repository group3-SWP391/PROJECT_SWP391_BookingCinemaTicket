package org.group3.project_swp391_bookingmovieticket.repositories;


import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {

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
}
