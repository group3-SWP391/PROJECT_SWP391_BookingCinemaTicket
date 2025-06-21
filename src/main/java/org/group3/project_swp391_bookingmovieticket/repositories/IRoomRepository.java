package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT s.room FROM Schedule s WHERE s.id = :scheduleId")
    Room findRoomBySchedule(@Param("scheduleId")Integer scheduleId);
}
