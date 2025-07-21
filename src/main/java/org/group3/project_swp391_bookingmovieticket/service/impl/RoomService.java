package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.repository.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService implements IRoomService {
    
    @Autowired
    private IRoomRepository roomRepository;
    
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    @Override
    public Optional<Room> getRoomById(Integer id) {
        return roomRepository.findById(id);
    }
    
    @Override
    public Room saveRoom(Room room) {
        if (room.getIsActive() == null) {
            room.setIsActive(1);
        }
        return roomRepository.save(room);
    }
    
    @Override
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }
    
    @Override
    public void deleteRoom(Integer id) {
        // Soft delete - deactivate instead of hard delete
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setIsActive(0);
            roomRepository.save(existingRoom);
        }
    }
    
    @Override
    public List<Room> getRoomsByBranchId(Integer branchId) {
        return roomRepository.findByBranchId(branchId);
    }
    
    @Override
    public List<Room> getActiveRoomsByBranchId(Integer branchId) {
        return roomRepository.findByBranchIdAndIsActive(branchId, 1);
    }
    
    @Override
    public Optional<Room> getRoomByNameAndBranchId(String name, Integer branchId) {
        return roomRepository.findByNameAndBranchId(name, branchId);
    }
    
    @Override
    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }
    
    @Override
    public List<Room> getRoomsByTypeAndBranch(String roomType, Integer branchId) {
        return roomRepository.findByRoomTypeAndBranchId(roomType, branchId);
    }
    
    @Override
    public List<Room> getRoomsByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        return roomRepository.findByCapacityBetween(minCapacity, maxCapacity);
    }
    
    @Override
    public List<Room> getRoomsWithMinCapacity(Integer capacity, Integer branchId) {
        return roomRepository.findByCapacityGreaterThanEqualAndBranchId(capacity, branchId);
    }
    
    @Override
    public List<Room> getActiveRooms() {
        return roomRepository.findByIsActive(1);
    }
    
    @Override
    public List<Room> getActiveRoomsWithBranch() {
        return roomRepository.findActiveRoomsWithBranch(1);
    }
    
    @Override
    public Long countRoomsByBranchId(Integer branchId) {
        return roomRepository.countRoomsByBranchId(branchId);
    }
    
    @Override
    public Long countActiveRoomsByBranchId(Integer branchId) {
        return roomRepository.countActiveRoomsByBranchId(branchId);
    }
    
    @Override
    public Long getTotalCapacityByBranchId(Integer branchId) {
        Long capacity = roomRepository.getTotalCapacityByBranchId(branchId);
        return capacity != null ? capacity : 0L;
    }
    
    @Override
    public List<Room> getLargestRoomsInBranch(Integer branchId) {
        return roomRepository.findLargestRoomInBranch(branchId);
    }
    
    @Override
    public List<Room> getActiveRoomsByType(String roomType) {
        return roomRepository.findByRoomTypeAndIsActive(roomType, 1);
    }
    
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findAll().stream()
                .map(Room::getRoomType)
                .filter(roomType -> roomType != null && !roomType.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(Integer id) {
        return roomRepository.existsById(id);
    }
    
    @Override
    public boolean isActiveRoom(Integer id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.isPresent() && room.get().getIsActive() == 1;
    }
    
    @Override
    public boolean isRoomNameUniqueInBranch(String name, Integer branchId) {
        return !roomRepository.findByNameAndBranchId(name, branchId).isPresent();
    }
    
    @Override
    public void activateRoom(Integer id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setIsActive(1);
            roomRepository.save(existingRoom);
        }
    }
    
    @Override
    public void deactivateRoom(Integer id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setIsActive(0);
            roomRepository.save(existingRoom);
        }
    }
    
    @Override
    public void updateSeatingLayout(Integer roomId, Integer rowCount, Integer seatsPerRow) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setRowCount(rowCount);
            existingRoom.setSeatsPerRow(seatsPerRow);
            
            // Update capacity based on seating layout
            if (rowCount != null && seatsPerRow != null) {
                existingRoom.setCapacity(rowCount * seatsPerRow);
            }
            
            roomRepository.save(existingRoom);
        }
    }
    
    @Override
    public boolean validateSeatingLayout(Integer capacity, Integer rowCount, Integer seatsPerRow) {
        if (rowCount == null || seatsPerRow == null || capacity == null) {
            return false;
        }
        
        // Check if calculated capacity matches provided capacity
        return capacity.equals(rowCount * seatsPerRow);
    }
} 