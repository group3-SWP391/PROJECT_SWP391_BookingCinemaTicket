package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISeatService extends IGeneralService<Seat> {
    List<SeatDTO> getSeatsByScheduleIdAndUserId(Integer scheduleId, Integer userId);
}
