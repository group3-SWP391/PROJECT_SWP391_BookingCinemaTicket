package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Integer> {
    
    // Find all rooms by cinema ID
    List<Room> findByCinemaId(Integer cinemaId);
    
    // Find active rooms by cinema ID
    List<Room> findByCinemaIdAndIsActive(Integer cinemaId, Integer isActive);
    
    // Find room by name and cinema ID
    Optional<Room> findByNameAndCinemaId(String name, Integer cinemaId);
    
    // Find rooms by room type
    List<Room> findByRoomType(String roomType);
    
    // Find rooms by room type and cinema ID
    List<Room> findByRoomTypeAndCinemaId(String roomType, Integer cinemaId);
    
    // Find rooms by capacity range
    List<Room> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);
    
    // Find rooms by capacity and cinema ID
    List<Room> findByCapacityGreaterThanEqualAndCinemaId(Integer capacity, Integer cinemaId);
    
    // Find all active rooms
    List<Room> findByIsActive(Integer isActive);
    
    // Custom query to find rooms with cinema details
    @Query("SELECT r FROM Room r JOIN FETCH r.cinema WHERE r.isActive = :isActive")
    List<Room> findActiveRoomsWithCinema(@Param("isActive") Integer isActive);
    
    // Find largest room in a cinema
    @Query("SELECT r FROM Room r WHERE r.cinema.id = :cinemaId AND r.isActive = 1 ORDER BY r.capacity DESC")
    List<Room> findLargestRoomInCinema(@Param("cinemaId") Integer cinemaId);
    
    // Count rooms by cinema ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.cinema.id = :cinemaId")
    Long countRoomsByCinemaId(@Param("cinemaId") Integer cinemaId);
    
    // Count active rooms by cinema ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.cinema.id = :cinemaId AND r.isActive = 1")
    Long countActiveRoomsByCinemaId(@Param("cinemaId") Integer cinemaId);
    
    // Get total capacity of all rooms in a cinema
    @Query("SELECT SUM(r.capacity) FROM Room r WHERE r.cinema.id = :cinemaId AND r.isActive = 1")
    Long getTotalCapacityByCinemaId(@Param("cinemaId") Integer cinemaId);
    
    // Find rooms by room type and active status
    List<Room> findByRoomTypeAndIsActive(String roomType, Integer isActive);
} 