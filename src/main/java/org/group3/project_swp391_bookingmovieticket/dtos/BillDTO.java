package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillDTO {
    private int id;
    private LocalDateTime createdTime;
    private List<TicketDTO> listTickets;
    private User user;
}
