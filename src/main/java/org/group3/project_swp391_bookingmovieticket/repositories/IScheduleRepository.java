package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT s FROM Schedule s WHERE s.branch.id = :branchId AND s.movie.name = :movieName AND s.startDate Between :startDate AND :endDate")
    List<Schedule> findByBranchMovieANDRange(@Param("branchId") Integer branchId, @Param("movieName") String movieName, @Param("startDate") LocalDate startTime, @Param("endDate") LocalDate endTime);
}
