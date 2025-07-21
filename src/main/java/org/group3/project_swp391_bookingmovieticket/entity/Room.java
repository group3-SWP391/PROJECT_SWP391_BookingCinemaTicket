package org.group3.project_swp391_bookingmovieticket.entity;

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

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private String active;

    @Column(name = "row_count")
    private Integer rowCount;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}

