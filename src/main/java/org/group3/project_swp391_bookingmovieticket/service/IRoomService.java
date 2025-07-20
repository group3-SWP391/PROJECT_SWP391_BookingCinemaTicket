package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.RoomDTO;

import java.util.List;

public interface IRoomService {
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Integer id);
}