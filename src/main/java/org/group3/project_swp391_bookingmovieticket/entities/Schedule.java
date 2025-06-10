package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "price")
    private Float price;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "start_time")
    private String startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "id") // Thay thế branchId bằng quan hệ
    private Branch branch;

    @Column(name = "room_id")
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    public Schedule() {
    }

    public Schedule(Integer id, Movie movie, Integer roomId, Branch branch, String startTime, String startDate, Float price) {
        this.id = id;
        this.movie = movie;
        this.roomId = roomId;
        this.branch = branch;
        this.startTime = startTime;
        this.startDate = startDate;
        this.price = price;
    }

}