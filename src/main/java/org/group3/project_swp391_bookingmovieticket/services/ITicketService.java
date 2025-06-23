package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Ticket;

import java.util.List;

public interface ITicketService extends IGeneralService<Ticket>{
    void saveAll(List<Ticket> tickets);
    Ticket findTicketsByOrderCode(long orderCode);
}
