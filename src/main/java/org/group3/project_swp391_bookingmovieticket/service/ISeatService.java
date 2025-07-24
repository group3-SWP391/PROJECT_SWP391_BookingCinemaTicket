package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;


import java.util.List;

public interface ISeatService extends IGeneralService<Seat> {
    List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId);
    List<String> findSeatNamesByIdList(List<Integer> ids);
    String findSeatNameById(Integer id);
    SeatDTO getSeatById(Integer id);
}
