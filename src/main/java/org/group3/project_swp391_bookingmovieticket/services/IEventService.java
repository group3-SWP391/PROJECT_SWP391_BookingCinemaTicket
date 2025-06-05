package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.EventDTO;

import java.util.List;

public interface IEventService extends IGeneralService<EventDTO> {
    List<EventDTO> findEventValid();
    EventDTO findEventById(int id);
}
