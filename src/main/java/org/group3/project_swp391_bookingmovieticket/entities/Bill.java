package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created_time")
    private LocalDateTime localDateTime;
    @Column(name = "price")
    private double price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;




}
