package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {

    private String code;
    private Integer userId;
    private Double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isUsed;
    private Integer orderId;
    private Double fixedDiscount;
    private Double minOrderValue;
    private String applicableEvents;
    private String applicableTicketTypes;
    private String applicableUserTypes;
    private Integer maxUsageCount;
    private Integer currentUsageCount;
    private String conditionText;
    private String imgVoucher;

}
