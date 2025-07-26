package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DirectorDTO {
    private int id;
    private String name;
    private String description;
    private String awards;
    private String nationality;
    private LocalDate birthDate;
    private String imageUrl;
}
