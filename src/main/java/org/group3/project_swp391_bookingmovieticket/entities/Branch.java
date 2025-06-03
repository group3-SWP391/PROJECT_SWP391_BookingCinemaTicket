package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "branch")
@NoArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "imgurl")
    private String imgurl;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phoneNo;

    @OneToMany(mappedBy = "branch",fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;
}
