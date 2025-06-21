package org.group3.project_swp391_bookingmovieticket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "branch")
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "imgurl")
    private String imgurl;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phoneNo;

    @ToString.Exclude
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}