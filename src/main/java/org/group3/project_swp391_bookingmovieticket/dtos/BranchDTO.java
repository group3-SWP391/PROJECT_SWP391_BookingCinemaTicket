package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BranchDTO {

    private int id;
    private String diaChi;
    private String imgURL;
    private String name;
    private String phoneNo;
    private List<ScheduleDTO> schedules;
    private List<MovieDTO> movies;
    private Long totalTicketSell;
}
