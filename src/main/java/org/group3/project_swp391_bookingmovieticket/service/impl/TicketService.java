package org.group3.project_swp391_bookingmovieticket.service.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.service.INotificationService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private INotificationService notificationService;

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
        Optional<User> user = userRepository.findById(id);
        System.out.println(user.isPresent() + "user bill");
        if (user.isEmpty()) {
            throw new IllegalArgumentException("ID " + id + " not found");
        }

        List<Bill> bills = billRepository.findByUserId(user.get().getId());
        System.out.println(bills + "bills");

        for (Bill b : bills) {
            List<Ticket> ticket = ticketRepository.findByBill_Id((b.getId()));
            ticketList.addAll(ticket);
        }
        return ticketList;
    }

    @Transactional
    public void checkTicketsAndSendNotifications() {
        List<Ticket> tickets = ticketRepository.findAll();
        Date currentTime = new Date();  // Lấy thời gian hiện tại

        for (Ticket ticket : tickets) {
            LocalTime endTimeLocal = ticket.getSchedule().getEndTime();
            LocalDateTime endDateTime = endTimeLocal.atDate(LocalDate.now());
            Date endTimeDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            Movie movie = ticket.getSchedule().getMovie();
            Hibernate.initialize(movie);
            User user = ticket.getBill().getUser();

            long timeDifference = currentTime.getTime() - endTimeDate.getTime();

            if (endTimeDate.before(currentTime)) {
                if (ticket.getStatus() && timeDifference >= 15 * 60 * 1000) {
                    notificationService.addNotification(user, movie);
                } else if (!ticket.getStatus() && timeDifference >= 15 * 60 * 1000) {
                    notificationService.sendNotificationIfNotWatched(user, movie);
                }
            }
        }
    }

}
