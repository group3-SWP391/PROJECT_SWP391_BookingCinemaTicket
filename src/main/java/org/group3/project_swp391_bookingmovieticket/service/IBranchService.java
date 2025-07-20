package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Branch;

import java.util.List;

public interface IBranchService extends IGeneralService<Branch>{
    void save(Branch entity);

    void delete(Integer id);

    List<Branch> findByDiaChi(String diaChi);

    List<BranchDTO> findAllBranchesDTO();
}
