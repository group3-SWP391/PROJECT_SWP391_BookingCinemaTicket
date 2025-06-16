package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Room;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    
    // Basic CRUD operations
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Integer id);
    Room saveRoom(Room room);
    Room updateRoom(Room room);
    void deleteRoom(Integer id);
    
    // Room by cinema
    List<Room> getRoomsByCinemaId(Integer cinemaId);
    List<Room> getActiveRoomsByCinemaId(Integer cinemaId);
    Optional<Room> getRoomByNameAndCinemaId(String name, Integer cinemaId);
    
    // Room types and capacity
    List<Room> getRoomsByType(String roomType);
    List<Room> getRoomsByTypeAndCinema(String roomType, Integer cinemaId);
    List<Room> getRoomsByCapacityRange(Integer minCapacity, Integer maxCapacity);
    List<Room> getRoomsWithMinCapacity(Integer capacity, Integer cinemaId);
    
    // Active rooms
    List<Room> getActiveRooms();
    List<Room> getActiveRoomsWithCinema();
    
    // Room statistics
    Long countRoomsByCinemaId(Integer cinemaId);
    Long countActiveRoomsByCinemaId(Integer cinemaId);
    Long getTotalCapacityByCinemaId(Integer cinemaId);
    List<Room> getLargestRoomsInCinema(Integer cinemaId);
    
    // Room type operations
    List<Room> getActiveRoomsByType(String roomType);
    List<String> getAllRoomTypes();
    
    // Validation
    boolean existsById(Integer id);
    boolean isActiveRoom(Integer id);
    boolean isRoomNameUniqueInCinema(String name, Integer cinemaId);
    
    // Activate/Deactivate
    void activateRoom(Integer id);
    void deactivateRoom(Integer id);
    
    // Seating layout
    void updateSeatingLayout(Integer roomId, Integer rowCount, Integer seatsPerRow);
    boolean validateSeatingLayout(Integer capacity, Integer rowCount, Integer seatsPerRow);
} 