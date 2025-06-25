package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "director")
@NoArgsConstructor
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "awards")
    private String awards;

    @Column(name = "nationality")
    private String nationality;

    @Column(name ="birth_date")
    private LocalDate birthDate;

    @Column(name = "image_url")
    private String imageUrl;
}
