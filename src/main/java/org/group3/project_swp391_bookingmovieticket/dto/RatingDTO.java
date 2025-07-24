package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private int id;
    private String name;
    private String description;
    private int ageLimit;
    private boolean isActive;
}
