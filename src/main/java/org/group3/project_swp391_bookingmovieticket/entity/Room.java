package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Table(name = "room")
@Entity
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "total_area")
    private double totalArea;

    @Column(name = "imgurl", length = 1000)
    private String imgURL;

    @Column(name = "description")
    private String description;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "row_count")
    private int rowCount;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

    @Column(name = "vip_seats", columnDefinition = "TEXT")
    private String vipSeats;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(nullable = false,name = "branch_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @Override
    public String toString() {
        return "hi";
    }
}
