package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Integer id;
    private Integer capacity;
    private String imgurl;
    private String name;
    private Double totalArea;
    private Branch branch;
    @ToString.Exclude
    private List<SeatDTO> seats;

}