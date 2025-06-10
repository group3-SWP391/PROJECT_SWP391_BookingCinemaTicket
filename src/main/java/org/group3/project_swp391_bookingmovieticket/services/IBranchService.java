package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;

import java.util.List;

public interface IBranchService extends IGeneralService<Branch>{
    void save(Branch entity);

    void delete(Integer id);

    List<Branch> findByDiaChi(String diaChi);

    List<BranchDTO> findAllBranchesDTO();
}
