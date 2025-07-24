package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> getSeatByRoom_Id(Integer roomId);

    @Query("SELECT s.name FROM Seat s WHERE s.id IN :ids")
    List<String> findSeatNamesByIdList(@Param("ids") List<Integer> ids);

    @Query("SELECT s.name FROM Seat s WHERE s.id = :id")
    String findSeatNameById(@Param("id") Integer id);

    List<Seat> findByRoomId(Integer roomId);

}
