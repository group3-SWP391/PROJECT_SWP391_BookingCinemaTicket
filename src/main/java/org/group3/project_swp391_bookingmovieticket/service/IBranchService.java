package org.group3.project_swp391_bookingmovieticket.service;


import org.group3.project_swp391_bookingmovieticket.dto.BranchDTO;

import java.util.List;

public interface IBranchService  extends IGeneralService<BranchDTO> {
    List<String> findAllLocationBranch();
    List<BranchDTO> findBranchByLocation(String location);
    List<BranchDTO> getBranchByMovie(Integer movieId);
    List<BranchDTO> getBranchByStartDate(Integer movieId, String startDate);
}
