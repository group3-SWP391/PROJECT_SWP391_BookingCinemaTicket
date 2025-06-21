package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Room;

public interface IRoomService extends IGeneralService<Room> {
    RoomDTO findRoomByScheduleId(Integer scheduleId);
}
