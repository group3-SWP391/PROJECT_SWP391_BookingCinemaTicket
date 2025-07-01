package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "[order]") // tránh lỗi từ khóa SQL
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "movie_name", nullable = false, length = 255)
    private String movieName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Transient
    private String transactionDateFormatted;
    @Column(name = "order_code")
    private Integer orderCode;

    @Column(name = "check_out_url", length = 1000)
    private String checkOutUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "seat_list", columnDefinition = "nvarchar(max)")
    private String seatList;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;


    @Column(name = "popcorn_drink_list", columnDefinition = "nvarchar(max)")
    private String popcornDrinkList;


    public Order(User user, Bill bill, String movieName, Seat seat, BigDecimal price, LocalDateTime transactionDate, String status) {
        this.user = user;
        this.bill = bill;
        this.movieName = movieName;
        this.seat = seat;
        this.price = price;
        this.transactionDate = transactionDate;
        this.status = status;
    }




}
