package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Room;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO implements Serializable {
    private Integer id;
    private Integer branchId;
    private String movieName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private Float price;
    private String imgurl;
    private Room room;
    private Movie movie;
    private BranchDTO branch;

}