package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

@Data
public class TicketDTO {

    private Integer id;
    private String qrImageUrl;
    private Integer billId;
    private Integer scheduleId;
    private Integer seatId;
    private boolean status;

    public TicketDTO() {}

    public TicketDTO(Integer id, String qrImageUrl, Integer billId, Integer scheduleId, Integer seatId, boolean status) {
        this.id = id;
        this.qrImageUrl = qrImageUrl;
        this.billId = billId;
        this.scheduleId = scheduleId;
        this.seatId = seatId;
        this.status = status;
    }
}
