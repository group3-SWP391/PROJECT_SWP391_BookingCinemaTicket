package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Room;
import org.group3.project_swp391_bookingmovieticket.repository.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoomService implements IRoomService {
    @Autowired
    private IRoomRepository roomRepository;
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {

        return roomRepository.findById(id);
    }

    @Override
    public void update(Room room) {

    }

    @Override
    public void remove(Integer id) {

    }


}
