package org.group3.project_swp391_bookingmovieticket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "room_type", length = 50)
    private String roomType; // STANDARD, VIP, IMAX, 4DX, etc.
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "is_active")
    private Integer isActive = 1;
    
    @Column(name = "row_count")
    private Integer rowCount;
    
    @Column(name = "seats_per_row")
    private Integer seatsPerRow;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonBackReference
    private Branch branch;
    
    // Constructors
    public Room() {}
    
    public Room(String name, Integer capacity, String roomType, Branch branch) {
        this.name = name;
        this.capacity = capacity;
        this.roomType = roomType;
        this.branch = branch;
        this.isActive = 1;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    
    public Integer getRowCount() {
        return rowCount;
    }
    
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
    
    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }
    
    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", roomType='" + roomType + '\'' +
                ", isActive=" + isActive +
                ", branchId=" + (branch != null ? branch.getId() : null) +
                '}';
    }
}

