package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Seat;

import java.util.List;

public interface ISeatService extends IGeneralService{
    List<Seat> getListSeatById(int roomId);
}
