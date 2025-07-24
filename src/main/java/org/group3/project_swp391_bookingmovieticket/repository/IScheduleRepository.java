package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT DISTINCT s.startDate FROM Schedule s WHERE s.movie.id = :movieId")
    List<LocalDate> getAllStartDateScheduleByMovieId(@Param("movieId") Integer movieId);

    @Query("SELECT s FROM Schedule s WHERE s.id= :scheduleId")
    Schedule findByScheduleId(@Param("scheduleId") Integer scheduleId);

    List<Schedule> findByBranchId(Integer branchId);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.movie WHERE s.id = :id")
    Optional<Schedule> findByIdWithMovie(@Param("id") Integer id);

}
