package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

import java.util.List;
@Data
public class BookingRequestDTO {
    private Integer userId;
    private Integer scheduleId;
    private List<Integer> listSeatId;
    private Integer totalPrice;
}
