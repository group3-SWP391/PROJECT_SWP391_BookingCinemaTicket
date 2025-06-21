package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT DISTINCT s.startDate FROM Schedule s WHERE s.movie.id = :movieId")
    List<LocalDate> getAllStartDateScheduleByMovieId(@Param("movieId") Integer movieId);

    @Query("SELECT s FROM Schedule s WHERE s.id= :scheduleId")
    Schedule findByScheduleId(@Param("scheduleId") Integer scheduleId);



}
