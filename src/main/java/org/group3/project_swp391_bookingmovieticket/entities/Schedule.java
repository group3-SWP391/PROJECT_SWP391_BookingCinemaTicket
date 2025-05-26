package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Table(name = "schedule")
@Entity
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "startTime")
    private LocalDate startTime;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

}
