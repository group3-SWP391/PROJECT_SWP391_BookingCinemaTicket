package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;

import java.util.List;

public interface ISeatService {
    SeatDTO getSeatById(Integer id);
}