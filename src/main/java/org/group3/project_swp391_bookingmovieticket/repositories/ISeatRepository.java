package org.group3.project_swp391_bookingmovieticket.repositories;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByRoomId(Integer roomId);
    List<Seat> findByIsActive(Boolean isActive);
    List<Seat> findByIsVip(Boolean isVip);
}
