package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;


@Data
public class BranchDTO {
    private Integer id;
    private String name;
    private String diaChi;

    public BranchDTO(Integer id, String name, String diaChi) {
        this.id = id;
        this.name = name;
        this.diaChi = diaChi;
    }


}