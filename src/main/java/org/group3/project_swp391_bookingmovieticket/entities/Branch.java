package org.group3.project_swp391_bookingmovieticket.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Room> rooms;

    public Branch() {
    }

    public Branch(Integer id, String location, String imgUrl, String name, String phoneNo, String description, List<Room> rooms) {
        this.id = id;
        this.location = location;
        this.imgUrl = imgUrl;
        this.name = name;
        this.phoneNo = phoneNo;
        this.description = description;
        this.rooms = rooms;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Integer getTotalCapacity() {
        if (rooms == null || rooms.isEmpty()) {
            return 0;
        }
        return rooms.stream()
                .filter(room -> room.getIsActive() == 1)
                .mapToInt(room -> room.getCapacity() != null ? room.getCapacity() : 0)
                .sum();
    }

    public Integer getActiveRoomCount() {
        if (rooms == null || rooms.isEmpty()) {
            return 0;
        }
        return (int) rooms.stream()
                .filter(room -> room.getIsActive() == 1)
                .count();
    }
}

