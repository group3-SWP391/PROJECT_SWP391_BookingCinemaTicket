package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(value = "SELECT COUNT(bill_id) FROM ticket WHERE schedule_id = :scheduleId", nativeQuery = true)
    int countTicketsByScheduleId(@Param("scheduleId") Integer scheduleId);
    List<Ticket> findByScheduleId(int id);
    List<Ticket> findByScheduleIdIn(List<Integer> scheduleId);
    List<Ticket> findByBillId(Integer id);
}
