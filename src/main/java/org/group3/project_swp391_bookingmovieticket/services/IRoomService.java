package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.RoomDTO;

import java.util.List;

public interface IRoomService {
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Integer id);
}