package org.group3.project_swp391_bookingmovieticket.utils.scheduled;


import org.group3.project_swp391_bookingmovieticket.service.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private TicketService ticketService;

    @Scheduled(fixedRate = 15 * 60 * 1000) // 15 phút
    public void checkTicketsAndSendNotifications() {
        ticketService.checkTicketsAndSendNotifications();
    }
}
