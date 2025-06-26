package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(value = "SELECT COUNT(t.bill_id) FROM ticket t JOIN schedule s ON t.schedule_id = s.id JOIN movie m ON s.movie_id = m.id WHERE m.name = :movieName", nativeQuery = true)
    int countTicketsByMovieName(@Param("movieName") String movieName);
    List<Ticket> findByScheduleId(int id);
}
