package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // map đúng tên cột trong database: price
    @Column(name = "price")
    private Double totalPrice;

    // map đúng tên cột trong database: created_time
    @Column(name = "created_time")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id") // đúng với cột user_id trong bảng bill
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = true) // nếu có
    private Schedule schedule;

    // nếu bạn muốn map ticketQuantity, bạn cần cột tương ứng trong bảng bill
    @Transient // tạm thời không ánh xạ với DB, vì DB không có cột này
    private int ticketQuantity;
}
