package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.repository.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {

    @Autowired
    private ISeatRepository seatRepository;

    @Override
    public List<Seat> getSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId);
    }

    @Override
    public List<Seat> getActiveSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId)
                .stream()
                .filter(seat -> seat.getIsActive() != null && seat.getIsActive())
                .collect(Collectors.toList());
    }

    @Override
    public List<Seat> getVipSeatsByRoomId(Integer roomId) {
        return seatRepository.findByRoomId(roomId)
                .stream()
                .filter(seat -> seat.getIsVip() != null && seat.getIsVip())
                .collect(Collectors.toList());
    }

    @Override
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    @Transactional
    public void deleteSeatsByRoomId(Integer roomId) {
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        seatRepository.deleteAll(seats);
    }

    @Override
    @Transactional
    public void generateSeatsForRoom(Room room, String vipSeatsInput) {
        // First, delete existing seats for this room
        deleteSeatsByRoomId(room.getId());

        // Parse VIP seats input
        List<String> vipSeatNames = parseVipSeats(vipSeatsInput);
        
        // Generate all possible seat names
        List<String> allSeatNames = generateAllSeatNames(room.getCapacity(), room.getRowCount());
        
        // Create seat entities
        List<Seat> seats = new ArrayList<>();
        for (String seatName : allSeatNames) {
            Seat seat = Seat.builder()
                    .name(seatName)
                    .room(room)
                    .isActive(room.getIsActive() == 1) // Inherit from room's active status
                    .isVip(vipSeatNames.contains(seatName)) // Check if seat is VIP (both already uppercase)
                    .build();
            seats.add(seat);
        }
        
        // Save all seats
        seatRepository.saveAll(seats);
    }

    @Override
    @Transactional
    public void updateSeatsForRoom(Room room, String vipSeatsInput) {
        // Parse VIP seats input
        List<String> vipSeatNames = parseVipSeats(vipSeatsInput);
        
        // Generate all possible seat names based on current capacity and row count
        List<String> allSeatNames = generateAllSeatNames(room.getCapacity(), room.getRowCount());
        
        // Get existing seats for this room
        List<Seat> existingSeats = seatRepository.findByRoomId(room.getId());
        
        // Update existing seats and track which ones we've processed
        Map<String, Seat> existingSeatMap = existingSeats.stream()
                .collect(Collectors.toMap(Seat::getName, seat -> seat));
        
        List<Seat> seatsToSave = new ArrayList<>();
        List<Seat> seatsToDelete = new ArrayList<>();
        
        // Update or create seats based on new layout
        for (String seatName : allSeatNames) {
            if (existingSeatMap.containsKey(seatName)) {
                // Update existing seat
                Seat existingSeat = existingSeatMap.get(seatName);
                existingSeat.setIsActive(room.getIsActive() == 1);
                existingSeat.setIsVip(vipSeatNames.contains(seatName));
                seatsToSave.add(existingSeat);
                existingSeatMap.remove(seatName); // Mark as processed
            } else {
                // Create new seat
                Seat newSeat = Seat.builder()
                        .name(seatName)
                        .room(room)
                        .isActive(room.getIsActive() == 1)
                        .isVip(vipSeatNames.contains(seatName))
                        .build();
                seatsToSave.add(newSeat);
            }
        }
        
        // Remaining seats in existingSeatMap are no longer needed
        // But we can't delete them if they have tickets, so just mark them as inactive
        for (Seat obsoleteSeat : existingSeatMap.values()) {
            obsoleteSeat.setIsActive(false);
            seatsToSave.add(obsoleteSeat);
        }
        
        // Save all updated/new seats
        seatRepository.saveAll(seatsToSave);
    }

    @Override
    public List<String> parseVipSeats(String vipSeatsInput) {
        if (vipSeatsInput == null || vipSeatsInput.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return Arrays.stream(vipSeatsInput.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(seat -> !seat.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> generateAllSeatNames(int capacity, int rowCount) {
        List<String> seatNames = new ArrayList<>();
        
        // Calculate seats per row
        int seatsPerRow = capacity / rowCount;
        int extraSeats = capacity % rowCount;
        
        for (int row = 0; row < rowCount; row++) {
            char rowLetter = (char) ('A' + row);
            
            // Calculate seats in this row (extra seats go to first few rows)
            int seatsInThisRow = seatsPerRow + (row < extraSeats ? 1 : 0);
            
            // Generate seat names for this row
            for (int seat = 1; seat <= seatsInThisRow; seat++) {
                seatNames.add(rowLetter + String.valueOf(seat));
            }
        }
        
        return seatNames;
    }
}