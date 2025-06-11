package org.group3.project_swp391_bookingmovieticket.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDTO  {
    private int id;
    private LocalDate startDate;
    private LocalTime startTime;
    private MovieDTO movie;
    private double price;
    private LocalDate endTime;
    private BranchDTO branch;
    private RoomDTO room;
}
