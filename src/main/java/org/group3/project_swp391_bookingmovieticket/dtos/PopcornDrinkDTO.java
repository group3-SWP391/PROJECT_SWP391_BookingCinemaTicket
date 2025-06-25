package org.group3.project_swp391_bookingmovieticket.dtos;
import lombok.Data;

@Data
public class PopcornDrinkDTO {
    private int id;
    private String name;
    private Double price;
    private String category;
    private String imageUrl;
    private String description;
    private Integer quantity;
}
