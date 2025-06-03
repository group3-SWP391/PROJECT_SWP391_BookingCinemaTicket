package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

@Data
public class SeatDTO {
    private int id;
    private String name;
    private boolean isActive;
    private boolean isVip;
    private int isOccupied;
    private boolean isChecked;
    private RoomDTO room;
}
