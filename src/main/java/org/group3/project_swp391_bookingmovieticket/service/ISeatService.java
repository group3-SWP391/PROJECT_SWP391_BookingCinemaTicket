package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Seat;

import java.util.List;

public interface ISeatService extends IGeneralService<Seat>{
    List<Seat> getListSeatById(int roomId);
    List<Seat> findSeatsByIds(List<Integer> ids);

}
