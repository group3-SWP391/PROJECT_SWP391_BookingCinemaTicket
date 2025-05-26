package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDTO {
    private int id;
    private LocalDate startDate;
    private LocalTime startTime;
    private MovieDTO movie;
    private Double price;
}
