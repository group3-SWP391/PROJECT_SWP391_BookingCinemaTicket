package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "price")
    private Float price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "format")
    private String format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @Column(name = "room_id")
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Column(name = "map")
    private String map;

    public Schedule() {
    }

    public Schedule(Integer id, Movie movie, Integer roomId, Branch branch, LocalTime startTime, LocalDate startDate, Float price, String format, String map) {
        this.id = id;
        this.movie = movie;
        this.roomId = roomId;
        this.branch = branch;
        this.startTime = startTime;
        this.startDate = startDate;
        this.price = price;
        this.format = format;
        this.map = map;
    }
}