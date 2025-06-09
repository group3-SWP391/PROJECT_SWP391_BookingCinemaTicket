package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "advertising_contact_request")
public class AdvertisingContactRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "notes")
    private String notes;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "rental_date", nullable = false)
    private Date rentalDate;
} 