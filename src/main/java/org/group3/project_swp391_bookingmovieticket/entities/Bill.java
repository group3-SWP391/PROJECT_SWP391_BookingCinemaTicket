package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Float price;
}
