package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class TicketService implements ITicketService {
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IBillRepository iBillRepository;
    @Autowired
    private ITicketRepository iTicketRepository;



    @Override
    public List<Bill> findAll() {
        return List.of();
    }

    @Override
    public Optional<Bill> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Bill bill) {

    }

    @Override
    public void remove(Integer id) {

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
