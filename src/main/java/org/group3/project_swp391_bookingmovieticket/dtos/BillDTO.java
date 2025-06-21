package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillDTO {

    private Integer id;
    private LocalDateTime createdTime;
    private Integer userId;
    private Float price;
}
