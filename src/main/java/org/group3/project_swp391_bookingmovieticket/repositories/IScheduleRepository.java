package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByBranchId(Integer branchId);
    @Query("SELECT s FROM Schedule s JOIN FETCH s.movie WHERE s.id = :id")
    Optional<Schedule> findByIdWithMovie(@Param("id") Integer id);

    @Query("SELECT s FROM Schedule s WHERE s.id= :scheduleId")
    Schedule findByScheduleId(@Param("scheduleId") Integer scheduleId);
}