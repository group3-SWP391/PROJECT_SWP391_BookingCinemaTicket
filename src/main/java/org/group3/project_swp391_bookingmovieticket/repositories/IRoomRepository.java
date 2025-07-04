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
    
    // Find all rooms by Branch ID
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
    @Query("SELECT r FROM Room r WHERE r.branch.id = :branchId AND r.isActive = 1 ORDER BY r.capacity DESC")
    List<Room> findLargestRoomInBranch(@Param("branchId") Integer branchId);
    
    // Count rooms by Branch ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :branchId")
    Long countRoomsByBranchId(@Param("branchId") Integer branchId);
    
    // Count active rooms by Branch ID
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :branchId AND r.isActive = 1")
    Long countActiveRoomsByBranchId(@Param("branchId") Integer branchId);
    
    // Get total capacity of all rooms in a Branch
    @Query("SELECT SUM(r.capacity) FROM Room r WHERE r.branch.id = :branchId AND r.isActive = 1")
    Long getTotalCapacityByBranchId(@Param("branchId") Integer branchId);
    
    // Find rooms by room type and active status
    List<Room> findByRoomTypeAndIsActive(String roomType, Integer isActive);
} 