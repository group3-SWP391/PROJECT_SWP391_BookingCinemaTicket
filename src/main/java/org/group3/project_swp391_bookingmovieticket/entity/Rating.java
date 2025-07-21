package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "age_limit")
    private Integer ageLimit;

    @Column(name = "is_active")
    private Integer isActive;
} 