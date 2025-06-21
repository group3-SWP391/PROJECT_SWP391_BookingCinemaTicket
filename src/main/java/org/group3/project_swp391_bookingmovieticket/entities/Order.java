package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "[order]")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "bill_id", nullable = false)
    private Integer billId;

    @Column(name = "movie_name", nullable = false, length = 255)
    private String movieName;

    @Column(name = "seat_id", nullable = false)
    private Integer seatId;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Transient
    private String transactionDateFormatted;

    public Order(Integer userId, Integer billId, String movieName, Integer seatId, BigDecimal price, LocalDateTime transactionDate, String status) {
        this.userId = userId;
        this.billId = billId;
        this.movieName = movieName;
        this.seatId = seatId;
        this.price = price;
        this.transactionDate = transactionDate;
        this.status = status;
    }
}