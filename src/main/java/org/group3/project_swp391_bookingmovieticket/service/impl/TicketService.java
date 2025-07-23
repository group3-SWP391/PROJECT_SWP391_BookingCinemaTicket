package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void update(Ticket ticket) {
        ticketRepository.save(ticket);

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
    public void confirmTicket(Integer id) {
        List<Ticket> ticketList = ticketRepository.findByBillId(id);
        for(Ticket ticket: ticketList){
            ticket.setStatus(false);
            update(ticket);

        }


    }

    @Override
    public boolean verifyEffectiveOrderCode(Integer id) {
        List<Ticket> ticketList = ticketRepository.findByBillId(id);
        for (Ticket ticket: ticketList){
            if(!ticket.getStatus()){
                return true;
            }
        }
        return false;

    }




    @Override
    public void remove(Integer id) {

    }
}
