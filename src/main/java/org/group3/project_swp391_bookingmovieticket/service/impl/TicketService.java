package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository ticketRepository;

    @Override
    public List<Ticket> findAll() {
        return List.of();
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public void update(Ticket ticket) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    @Override
    public List<Ticket> findTicketsByOrderCode(long orderCode) {
        return ticketRepository.findTicketsByOrderCode(orderCode);
    }

    @Override
    public List<Ticket> findTicketsBySchedule_IdAndSeat_Id(Integer scheduleId, Integer seatId) {
        return ticketRepository.findTicketsBySchedule_IdAndSeat_Id(scheduleId, seatId);
    }

    @Override
    public List<Ticket> getListBillByID(int id) {
        List<Ticket> ticketList = new ArrayList<>();
        Optional<User> user = iUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("ID "+ id + " not found");
        }

        List<Bill> bills = iBillRepository.findByUserId(user.get().getId());


        for (Bill b : bills) {
            Optional<Ticket> ticketOptional = iTicketRepository.findById(b.getId());
            if(ticketOptional.isPresent()){
                ticketList.add(ticketOptional.get());
            }


        }
        return ticketList;


    }
}
