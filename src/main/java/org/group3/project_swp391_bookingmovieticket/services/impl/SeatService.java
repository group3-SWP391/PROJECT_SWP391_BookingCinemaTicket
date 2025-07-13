package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.repositories.ISeatRepository;
import org.group3.project_swp391_bookingmovieticket.services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SeatService implements ISeatService {
    @Autowired
    private ISeatRepository iSeatRepository;
    @Override
    public List<Seat> getListSeatById(int roomId) {
        return iSeatRepository.findByRoomId(roomId);
    }

    @Override
    public List<Seat> findSeatsByIds(List<Integer> ids) {
        return iSeatRepository.findByIdIn(ids);
    }



    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {
        return iSeatRepository.findById(id);
    }



    @Override
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }
}
