package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "seat")
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_vip", nullable = false)
    private Boolean isVip;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule; // hoặc
    @ManyToOne
    private Ticket ticket; // và Ticket có schedule

}