package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

@Data
public class TicketDTO {

    private Integer id;
    private String qrImageUrl;
    private Integer billId;
    private Integer scheduleId;
    private Integer seatId;
}
