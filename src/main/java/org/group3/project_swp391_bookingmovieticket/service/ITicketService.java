package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;

import java.util.List;

public interface ITicketService extends IGeneralService<Bill> {
    List<Ticket> getListBillByID(int id);


}