package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
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

//phu
@Query("SELECT s FROM Schedule s WHERE s.branch.id = :branchId AND s.movie.name = :movieName AND s.startDate Between :startDate AND :endDate")
List<Schedule> findByBranchMovieANDRange(@Param("branchId") Integer branchId, @Param("movieName") String movieName, @Param("startDate") LocalDate startTime, @Param("endDate") LocalDate endTime);
}
