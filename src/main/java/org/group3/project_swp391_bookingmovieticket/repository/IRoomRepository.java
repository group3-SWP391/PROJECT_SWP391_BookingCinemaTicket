package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IRoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT s.room FROM Schedule s WHERE s.id = :scheduleId")
    Room findRoomBySchedule(@Param("scheduleId")Integer scheduleId);
    List<Room> findByBranchId(Integer branchId);

    // Find active rooms by Branch ID
    List<Room> findByBranchIdAndIsActive(Integer branchId, Integer isActive);

    // Find room by name and Branch ID
    Optional<Room> findByNameAndBranchId(String name, Integer branchId);

    // Find rooms by room type
    List<Room> findByRoomType(String roomType);

    // Find rooms by room type and Branch ID
    List<Room> findByRoomTypeAndBranchId(String roomType, Integer branchId);

    // Find rooms by capacity range
    List<Room> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);

    // Find rooms by capacity and Branch ID
    List<Room> findByCapacityGreaterThanEqualAndBranchId(Integer capacity, Integer branchId);

    // Find all active rooms
    List<Room> findByIsActive(Integer isActive);

    // Custom query to find rooms with Branch details
    @Query("SELECT r FROM Room r JOIN FETCH r.branch WHERE r.isActive = :isActive")
    List<Room> findActiveRoomsWithBranch(@Param("isActive") Integer isActive);

    // Find largest room in a Branch
    @Query("SELECT r FROM Room r WHERE r.branch.id = :branchId AND r.isActive = true ORDER BY r.capacity DESC")
    List<Room> findLargestRoomInBranch(@Param("branchId") Integer branchId);

    // Count rooms by Branch ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :branchId")
    Long countRoomsByBranchId(@Param("branchId") Integer branchId);

    // Count active rooms by Branch ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :branchId AND r.isActive = true")
    Long countActiveRoomsByBranchId(@Param("branchId") Integer branchId);

    // Get total capacity of all rooms in a Branch
    @Query("SELECT SUM(r.capacity) FROM Room r WHERE r.branch.id = :branchId AND r.isActive = true")
    Long getTotalCapacityByBranchId(@Param("branchId") Integer branchId);

    // Find rooms by room type and active status
    List<Room> findByRoomTypeAndIsActive(String roomType, Integer isActive);
}

