package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SeatDTO {
    private int id;
    private String name;
    private boolean isActive;
    private boolean isVip;
    private boolean isOccupied;
    private boolean isChecked;

}
