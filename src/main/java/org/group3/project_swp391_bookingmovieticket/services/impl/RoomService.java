package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.repositories.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.services.IRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Room> findAll() {
        return List.of();
    }

    @Override
    public Optional<Room> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Room room) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public RoomDTO findRoomByScheduleId(Integer scheduleId) {
        Room room = roomRepository.findRoomBySchedule(scheduleId);
        return modelMapper.map(room, RoomDTO.class);
    }
}
