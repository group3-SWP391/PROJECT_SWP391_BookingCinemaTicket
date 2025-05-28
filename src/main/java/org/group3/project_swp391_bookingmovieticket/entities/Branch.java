package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dia_chi")
    private String diaChi;

    private String imgurl;

    private String name;

    @Column(name = "phone_no")
    private String phoneNo;
}

