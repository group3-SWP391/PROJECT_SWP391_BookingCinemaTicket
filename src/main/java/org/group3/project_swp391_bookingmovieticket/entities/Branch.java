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

    @Column(name = "location")
    private String location;

    @Column(name = "img_url")
    private String imgURL;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "branch",fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;
}
