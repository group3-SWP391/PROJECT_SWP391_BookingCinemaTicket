package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private Double discountPercentage;
    private Double fixedDiscount;
    private Double minOrderValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isUsed;
    private String applicableEvents;
    private String applicableTicketTypes;
    private String applicableUserTypes;
    private Integer maxUsageCount;
    private Integer currentUsageCount;
    private String conditionText;
    private String imgVoucher;
}