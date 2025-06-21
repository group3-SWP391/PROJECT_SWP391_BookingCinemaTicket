package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.entities.Room;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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