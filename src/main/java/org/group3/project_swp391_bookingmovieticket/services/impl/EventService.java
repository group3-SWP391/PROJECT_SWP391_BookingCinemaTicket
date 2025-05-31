package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.EventDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.repositories.IEventRepository;
import org.group3.project_swp391_bookingmovieticket.services.IEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<String> findAll() {
        return List.of();
    }

    @Override
    public Optional<String> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(String s) {

    }

    @Override
    public void remove(Integer id) {

    }


    @Override
    public List<EventDTO> findEventValid() {
        return eventRepository.findEventValid()
                .stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }
}
