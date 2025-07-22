package org.group3.project_swp391_bookingmovieticket.service.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.service.INotificationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private INotificationService notificationService;

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
