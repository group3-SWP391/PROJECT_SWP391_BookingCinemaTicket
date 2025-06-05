package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {
    // Nếu muốn lấy tất cả schedule có ngày startDate >= ngày cho trước:
    List<Schedule> findByStartDateAfter(java.time.LocalDate date);

    // Hoặc lấy schedule theo movieId:
    List<Schedule> findByMovieId(Long movieId);
}