package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
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
    @Column(name = "user_id")
    private Integer userId;
    private String applicableEvents;
    private String applicableTicketTypes;
    private String applicableUserTypes;
    private Integer maxUsageCount;
    private Integer currentUsageCount;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public Double getFixedDiscount() { return fixedDiscount; }
    public void setFixedDiscount(Double fixedDiscount) { this.fixedDiscount = fixedDiscount; }
    public Double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(Double minOrderValue) { this.minOrderValue = minOrderValue; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { this.isUsed = used; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getApplicableEvents() { return applicableEvents; }
    public void setApplicableEvents(String applicableEvents) { this.applicableEvents = applicableEvents; }
    public String getApplicableTicketTypes() { return applicableTicketTypes; }
    public void setApplicableTicketTypes(String applicableTicketTypes) { this.applicableTicketTypes = applicableTicketTypes; }
    public String getApplicableUserTypes() { return applicableUserTypes; }
    public void setApplicableUserTypes(String applicableUserTypes) { this.applicableUserTypes = applicableUserTypes; }
    public Integer getMaxUsageCount() { return maxUsageCount; }
    public void setMaxUsageCount(Integer maxUsageCount) { this.maxUsageCount = maxUsageCount; }
    public Integer getCurrentUsageCount() { return currentUsageCount; }
    public void setCurrentUsageCount(Integer currentUsageCount) { this.currentUsageCount = currentUsageCount; }
}