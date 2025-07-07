package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.repositories.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Service
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository ticketRepository;
    @Override
    public List findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional findById(Integer id) {
        return Optional.empty();
    }



    @Override
    public void update(Object o) {

    }

    @Override
    public HashSet<Integer> findBookedSeatIdsBySchedule(int id) {
        HashSet<Integer> integerHashSet = new HashSet<>();
        List<Ticket> ticketList = findByScheduleId(id);
        for (Ticket ticket: ticketList){
            integerHashSet.add(ticket.getSeat().getId());
        }
        return integerHashSet;
    }

    @Override
    public List<Ticket> findByScheduleId(int id) {
        return ticketRepository.findByScheduleId(id);
    }

    @Override
    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }


    @Override
    public void remove(Integer id) {

    }
}
