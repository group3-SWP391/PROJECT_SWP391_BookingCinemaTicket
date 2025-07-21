package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.EventDTO;

import java.util.List;

public interface IEventService extends IGeneralService<EventDTO> {
    List<EventDTO> findEventValid();
    EventDTO findEventById(int id);
}
