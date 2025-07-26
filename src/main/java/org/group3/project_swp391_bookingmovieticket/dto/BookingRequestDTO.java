package org.group3.project_swp391_bookingmovieticket.dto;

import lombok.Data;

import java.util.List;
@Data
public class BookingRequestDTO {
    private Integer userId;
    private Integer scheduleId;
    private List<Integer> listSeatId;
    private List<PopcornDrinkDTO> listPopcornDrink;
    private Integer totalPrice;
    private Integer movieId;

    public BookingRequestDTO(Integer userId, Integer scheduleId, List<Integer> listSeatId, Integer totalPrice, Integer movieId) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.listSeatId = listSeatId;
        this.totalPrice = totalPrice;
        this.movieId = movieId;
    }
}
