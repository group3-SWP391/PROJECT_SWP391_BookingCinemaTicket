package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.dtos.TicketDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.services.IGeneralService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findTicketsBySchedule_Id(Integer scheduleId);
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.bill b " +
            "WHERE b.user.id = :userId AND t.schedule.id = :scheduleId " +
            "ORDER BY t.id DESC")
    List<Ticket> findTicketsByUserIdAndScheduleId(@Param("userId") Integer userId,
                                                  @Param("scheduleId") Integer scheduleId);
}
