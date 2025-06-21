package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;
import lombok.ToString;

@Data
public class SeatDTO {
    private int id;
    private String name;
    private boolean isActive;
    private boolean isVip;
    private boolean isOccupied;
    private boolean isChecked;

}
