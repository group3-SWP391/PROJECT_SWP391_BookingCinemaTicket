package org.group3.project_swp391_bookingmovieticket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "branch")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column(name = "map")
    private String map;

    @ToString.Exclude
    @JsonBackReference
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;

    @ToString.Exclude
    @JsonBackReference
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<Room> roomList;

    public Integer getTotalCapacity() {
        if (roomList == null || roomList.isEmpty()) {
            return 0;
        }
        return roomList.stream()
                .filter(Room::isActive)
                .mapToInt(Room::getCapacity)
                .sum();
    }

    public Integer getActiveRoomCount() {
        if (roomList == null || roomList.isEmpty()) {
            return 0;
        }
        return (int) roomList.stream()
                .filter(Room::isActive)
                .count();
    }
}
