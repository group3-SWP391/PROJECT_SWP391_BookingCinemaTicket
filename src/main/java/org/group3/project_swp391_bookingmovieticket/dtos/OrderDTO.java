package org.group3.project_swp391_bookingmovieticket.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {

    private Integer id;
    private Integer userId;
    private Integer billId;
    private String movieName;
    private Integer seatId;
    private BigDecimal price;
    private LocalDateTime transactionDate;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Integer id, Integer userId, Integer billId, String movieName, Integer seatId, BigDecimal price, LocalDateTime transactionDate, String status) {
        this.id = id;
        this.userId = userId;
        this.billId = billId;
        this.movieName = movieName;
        this.seatId = seatId;
        this.price = price;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}