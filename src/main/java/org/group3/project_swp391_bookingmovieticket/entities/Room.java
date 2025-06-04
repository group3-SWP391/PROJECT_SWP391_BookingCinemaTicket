package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int capacity;

    private String imgurl;

    private String name;

    @Column(name = "total_area")
    private double totalArea;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}

