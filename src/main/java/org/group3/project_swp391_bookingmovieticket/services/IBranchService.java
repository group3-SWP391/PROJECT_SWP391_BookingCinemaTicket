package org.group3.project_swp391_bookingmovieticket.services;


import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBranchService  extends IGeneralService<BranchDTO> {
    List<String> findAllLocationBranch();
    List<BranchDTO> findBranchByLocation(String location);
    List<BranchDTO> getBranchByMovie(Integer movieId);
    List<BranchDTO> getBranchByStartDate(Integer movieId, String startDate);
}
