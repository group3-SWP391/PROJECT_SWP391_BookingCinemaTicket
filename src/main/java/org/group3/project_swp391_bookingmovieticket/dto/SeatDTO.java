package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer id;
    private String name;
    private Boolean isActive;
    private Boolean isVip;
    private boolean isOccupied = false;
    private boolean isChecked;
}