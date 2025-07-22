package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Ticket;

import java.util.List;

public interface ITicketService extends IGeneralService<Ticket>{
    void saveAll(List<Ticket> tickets);
    List<Ticket> findTicketsByOrderCode(long orderCode);
    List<Ticket> findTicketsBySchedule_IdAndSeat_Id(Integer scheduleId, Integer seatId);
    List<Ticket> getListBillByID(int id);
}