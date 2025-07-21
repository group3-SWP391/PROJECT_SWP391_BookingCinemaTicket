package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class RoomDTO {
    private int id;
    private String name;
    private int capacity;
    private double totalArea;
    private String imgURL;
    private BranchDTO branch;
    private int rowCount;
    private boolean isActive;
    private String roomType;
    private String description;
    @ToString.Exclude
    private List<SeatDTO> seats;
}
