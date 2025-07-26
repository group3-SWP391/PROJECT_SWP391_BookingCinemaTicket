package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.status = true AND e.startDate <= CURRENT_TIMESTAMP ")
    List<Event> findEventValid();

    Event findEventById(int id);
}
