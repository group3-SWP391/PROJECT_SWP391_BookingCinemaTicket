package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Room;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    
    // Basic CRUD operations
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Integer id);
    Room saveRoom(Room room);
    Room updateRoom(Room room);
    void deleteRoom(Integer id);
    
    // Room by branch
    List<Room> getRoomsByBranchId(Integer branchId);
    List<Room> getActiveRoomsByBranchId(Integer branchId);
    Optional<Room> getRoomByNameAndBranchId(String name, Integer branchId);
    
    // Room types and capacity
    List<Room> getRoomsByType(String roomType);
    List<Room> getRoomsByTypeAndBranch(String roomType, Integer branchId);
    List<Room> getRoomsByCapacityRange(Integer minCapacity, Integer maxCapacity);
    List<Room> getRoomsWithMinCapacity(Integer capacity, Integer branchId);
    
    // Active rooms
    List<Room> getActiveRooms();
    List<Room> getActiveRoomsWithBranch();
    
    // Room statistics
    Long countRoomsByBranchId(Integer branchId);
    Long countActiveRoomsByBranchId(Integer branchId);
    Long getTotalCapacityByBranchId(Integer branchId);
    List<Room> getLargestRoomsInBranch(Integer branchId);
    
    // Room type operations
    List<Room> getActiveRoomsByType(String roomType);
    List<String> getAllRoomTypes();
    
    // Validation
    boolean existsById(Integer id);
    boolean isActiveRoom(Integer id);
    boolean isRoomNameUniqueInBranch(String name, Integer branchId);
    
    // Activate/Deactivate
    void activateRoom(Integer id);
    void deactivateRoom(Integer id);
    
    // Seating layout
    void updateSeatingLayout(Integer roomId, Integer rowCount, Integer seatsPerRow);
    boolean validateSeatingLayout(Integer capacity, Integer rowCount, Integer seatsPerRow);
} 