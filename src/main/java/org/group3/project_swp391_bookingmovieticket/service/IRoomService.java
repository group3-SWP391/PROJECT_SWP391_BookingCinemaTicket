package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Room;

public interface IRoomService extends IGeneralService<Room> {
    RoomDTO findRoomByScheduleId(Integer scheduleId);
}
