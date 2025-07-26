package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.User;

import java.time.LocalDateTime;

@Data
public class PaymentLinkDTO {
    private long orderCode;
    private String checkoutUrl;
    private String status;
    private String seatList;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private User user;
    private Schedule schedule;
    private Integer billId;
}
