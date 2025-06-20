package org.group3.project_swp391_bookingmovieticket.services.impl;

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
    public HashMap<Ticket, Integer> getMovieStatusByTicketCount(String date, String fromHour, String branch){
        LocalTime t1 = LocalTime.parse(fromHour);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Ticket> listInitial = findAll();
        List<Ticket> ticketList = new ArrayList<>();
        for(Ticket ticket: listInitial){
            if(dateFormat.format(ticket.getSchedule().getStartDate()).equals(date) && ticket.getSchedule().getStartTime().toLocalTime().getHour() == t1.getHour()
            && ticket.getSchedule().getBranch().getName().equals(branch)){
                ticketList.add(ticket);

            }
        }
        HashMap<Ticket, Integer> ticketIntegerHashMap = new HashMap<>();
        if(!ticketList.isEmpty()){
            String movieNameInitial = "";
            for (Ticket ticket: ticketList){
                if(!ticket.getSchedule().getMovie().getName().equals(movieNameInitial)){
                    int count = ticketRepository.countTicketsByMovieName(ticket.getSchedule().getMovie().getName());
                    ticketIntegerHashMap.put(ticket, count);
                    movieNameInitial = ticket.getSchedule().getMovie().getName();
                }
            }
            return ticketIntegerHashMap;



        }else {
            return ticketIntegerHashMap;
        }

    }

    @Override
    public void remove(Integer id) {

    }
}
