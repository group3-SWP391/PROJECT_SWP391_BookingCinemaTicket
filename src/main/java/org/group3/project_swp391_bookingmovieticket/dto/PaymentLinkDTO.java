package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkDTO {

    private Integer id;
    private Integer userId;
    private Integer billId;
    private String movieName;
    private Integer seatId;
    private BigDecimal price;
    private LocalDateTime transactionDate;
    private String status;
    private Integer orderCode;
    private String checkOutUrl;
    private Integer scheduleId;
    private String seatList;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private String popcornDrinkList;
}
