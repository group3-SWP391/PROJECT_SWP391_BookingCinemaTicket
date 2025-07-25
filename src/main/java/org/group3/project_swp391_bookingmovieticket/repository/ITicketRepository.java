package org.group3.project_swp391_bookingmovieticket.repository;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
    // Thêm method custom nếu cần, ví dụ:
    List<Ticket> findByBillId(Integer billId);
    List<Ticket> findByScheduleId(Integer scheduleId);
    List<Ticket> findBySeatId(Integer seatId);
}