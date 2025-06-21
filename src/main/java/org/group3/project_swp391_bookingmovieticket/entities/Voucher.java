package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Changed from Long to Integer
    private String code;
    private Double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isUsed;
    @Column(name = "user_id")
    private Integer userId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { this.isUsed = used; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}