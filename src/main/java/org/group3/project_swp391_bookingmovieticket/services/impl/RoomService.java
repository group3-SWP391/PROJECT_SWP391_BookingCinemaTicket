package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.repositories.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoomService implements IRoomService {
    @Autowired
    private IRoomRepository iRoomRepository;
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {
        return iRoomRepository.findById(id);
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }
}
