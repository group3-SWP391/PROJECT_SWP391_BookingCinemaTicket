package org.group3.project_swp391_bookingmovieticket.repositories;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
    // Thêm method custom nếu cần, ví dụ:
    List<Ticket> findByBillId(Integer billId);
    List<Ticket> findByScheduleId(Integer scheduleId);
}