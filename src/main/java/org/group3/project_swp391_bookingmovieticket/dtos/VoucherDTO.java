package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoucherDTO {

    private String code;
    private Integer userId;
    private Double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isUsed;
    private Integer orderId;

    public VoucherDTO() {}

    public VoucherDTO(String code, Integer userId, Double discountPercentage, LocalDateTime startDate, LocalDateTime endDate, Boolean isUsed, Integer orderId) {
        this.code = code;
        this.userId = userId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isUsed = isUsed;
        this.orderId = orderId;
    }
}
