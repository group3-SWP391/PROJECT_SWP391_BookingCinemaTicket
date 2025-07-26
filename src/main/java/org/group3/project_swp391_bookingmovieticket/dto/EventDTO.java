package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {
    private int id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer branchId;
    private Integer movieId;
    private String imageLargeURL;
    private String imageSmallURL;
    private Boolean status;
}
