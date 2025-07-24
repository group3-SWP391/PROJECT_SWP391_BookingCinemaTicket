package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Table(name = "ticket")
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "qr_imageurl")
    private String qrImageURL;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = false, name = "seat_id")
    private Seat seat;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = false, name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "popcorn_drink_list")
    private String listPopcornDrinkName;

    @ManyToOne
    @JoinColumn(name = "payment_link_id")
    private PaymentLink paymentLink;

}
