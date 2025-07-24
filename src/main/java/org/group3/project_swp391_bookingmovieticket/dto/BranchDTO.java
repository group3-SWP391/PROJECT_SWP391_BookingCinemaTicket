package org.group3.project_swp391_bookingmovieticket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

    private int id;
    private String location;
    private String imgURL;
    private String name;
    private String phoneNo;
    private String description;
    private String locationDetail;
    @JsonIgnore
    @ToString.Exclude
    private List<ScheduleDTO> scheduleList;
    @JsonIgnore
    @ToString.Exclude
    private List<RoomDTO> roomList;
    private Long totalTicketSell;
    @ToString.Exclude
    private Map<String, List<ScheduleDTO>> groupedScheduleByRoomName;

    public BranchDTO(Integer id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
