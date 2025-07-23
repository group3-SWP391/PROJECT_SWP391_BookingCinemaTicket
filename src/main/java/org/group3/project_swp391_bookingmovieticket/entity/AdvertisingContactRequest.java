package org.group3.project_swp391_bookingmovieticket.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "advertising_contact_request")
@NoArgsConstructor
public class AdvertisingContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_address", length = 255)
    private String companyAddress;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "notes")
    private String notes;

    @Column(name = "phone")
    private String phone;

    @Column(name = "rental_date")
    private LocalDate rentalDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
