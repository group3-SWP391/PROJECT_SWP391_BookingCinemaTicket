package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO implements Serializable {
    private Integer id;
    private Integer branchId;
    private String movieName;
    private String startDate;
    private String startTime;
    private Float price;
    private String imgurl;
    private Integer roomId;
    private Integer movieId;

}