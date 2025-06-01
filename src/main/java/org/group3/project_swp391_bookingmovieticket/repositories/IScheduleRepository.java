package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT MAX(s.startDate) FROM Schedule s WHERE s.movie.id = :movieId")
    java.sql.Date getLastShowDateByMovie(@Param("movieId") int movieId);
}
