package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    
    List<Schedule> findByBranchId(Integer branchId);
    
    List<Schedule> findByMovieId(Integer movieId);
    
    List<Schedule> findByRoomId(Integer roomId);
    
    List<Schedule> findByStartDate(LocalDate startDate);
    
    @Query("SELECT s FROM Schedule s WHERE s.branch.id = :branchId AND s.startDate = :startDate")
    List<Schedule> findByBranchIdAndStartDate(@Param("branchId") Integer branchId, @Param("startDate") LocalDate startDate);
    
    @Query("SELECT s FROM Schedule s WHERE s.room.id = :roomId AND s.startDate = :startDate")
    List<Schedule> findSchedulesByRoomAndDate(@Param("roomId") Integer roomId, 
                                            @Param("startDate") LocalDate startDate);
    
    @Query("SELECT s FROM Schedule s JOIN FETCH s.branch JOIN FETCH s.movie JOIN FETCH s.room WHERE s.id = :id")
    Optional<Schedule> findByIdWithDetails(@Param("id") Integer id);
    
    @Query("SELECT MAX(s.startDate) FROM Schedule s WHERE s.movie.id = :movieId")
    Date getLastShowDateByMovie(@Param("movieId") Integer movieId);
}
