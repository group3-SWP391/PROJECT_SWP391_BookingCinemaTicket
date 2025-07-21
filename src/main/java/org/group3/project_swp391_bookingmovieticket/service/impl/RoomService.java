package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private IRoomRepository IRoomRepository;

    public List<RoomDTO> getAllRooms() {
        return IRoomRepository.findAll().stream()
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
        Room room = IRoomRepository.findById(id).orElse(null);
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