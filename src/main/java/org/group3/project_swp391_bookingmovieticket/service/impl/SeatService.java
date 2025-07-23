package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.repository.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SeatService implements ISeatService {
    @Autowired
    private ISeatRepository seatRepository;
    @Override
    public List<Seat> getListSeatById(int roomId) {

        return seatRepository.findByRoomId(roomId);
    }

    @Override
    public List<Seat> findSeatsByIds(List<Integer> ids) {

        return seatRepository.findByIdIn(ids);
    }



    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {

        return seatRepository.findById(id);
    }

    @Override
    public void update(Seat seat) {

    }




    @Override
    public void remove(Integer id) {

    }
}
