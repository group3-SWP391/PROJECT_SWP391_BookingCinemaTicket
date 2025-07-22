package org.group3.project_swp391_bookingmovieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "branch")
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "location_detail")
    private String locationDetail;

    @ToString.Exclude
    @OneToMany(mappedBy = "branch",fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;

    @ToString.Exclude
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<Room> roomList;
}
