package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> {
                    RoomDTO dto = new RoomDTO();
                    dto.setId(room.getId());
                    dto.setCapacity(room.getCapacity());
                    dto.setImgurl(room.getImgurl());
                    dto.setName(room.getName());
                    dto.setTotalArea(room.getTotalArea());
                    dto.setBranch(room.getBranch());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public RoomDTO getRoomById(Integer id) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) return null;
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setCapacity(room.getCapacity());
        dto.setImgurl(room.getImgurl());
        dto.setName(room.getName());
        dto.setTotalArea(room.getTotalArea());
        dto.setBranch(room.getBranch());
        return dto;
    }
}