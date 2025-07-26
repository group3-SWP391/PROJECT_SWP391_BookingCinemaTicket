package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "rating")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "age_limit")
    private int ageLimit;

    @Column(name = "is_active")
    private boolean isActive;

}
