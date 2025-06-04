package org.group3.project_swp391_bookingmovieticket.services;


import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;

import java.util.List;

public interface IBranchService  extends IGeneralService<BranchDTO> {
    List<String> findAllLocationBranch();
}
