package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "action_type", nullable = false)
    private String actionType; // e.g., "CHANGE_PASSWORD" or "UPDATE_PROFILE"

    // Manual setter if Lombok fails
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}