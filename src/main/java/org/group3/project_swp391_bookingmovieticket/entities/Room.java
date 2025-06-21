package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "room")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "imgurl")
    private String imgurl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "total_area", nullable = false)
    private Double totalArea;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

}