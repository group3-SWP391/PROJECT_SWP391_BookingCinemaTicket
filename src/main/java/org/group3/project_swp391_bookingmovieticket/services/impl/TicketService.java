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
    public HashMap<Ticket, Integer> getMovieStatusByTicketCount(List<Schedule> scheduleList){
        List<Ticket> ticketList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (Schedule schedule:  scheduleList){
           integerList.add(schedule.getId());

        }
        ticketList = ticketRepository.findByScheduleIdIn(integerList);
        HashMap<Ticket, Integer> ticketIntegerHashMap = new HashMap<>();
        int scheduleIdInitial = -1;
        for (Ticket ticket: ticketList){
            if(ticket.getSchedule().getId() != scheduleIdInitial){
                int count = ticketRepository.countTicketsByScheduleId(ticket.getSchedule().getId());
                ticketIntegerHashMap.put(ticket, count);
                scheduleIdInitial = ticket.getSchedule().getId();

            }

        }
        return ticketIntegerHashMap;

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
