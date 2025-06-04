package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "event")
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime  startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    // Quan hệ ManyToOne với Movie
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
