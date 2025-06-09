package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
public class BranchDTO {

    private int id;
    private String location;
    private String imgURL;
    private String name;
    private String phoneNo;
    private String description;
    private String locationDetail;
    @ToString.Exclude
    private List<ScheduleDTO> scheduleList;
    @ToString.Exclude
    private List<RoomDTO> roomList;
    private Long totalTicketSell;
    @ToString.Exclude
    private Map<String, List<ScheduleDTO>> groupedScheduleByRoomName;

}
