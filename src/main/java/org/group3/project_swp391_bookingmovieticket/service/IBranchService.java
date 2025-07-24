package org.group3.project_swp391_bookingmovieticket.service;


import org.group3.project_swp391_bookingmovieticket.dto.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Branch;

import java.util.List;
import java.util.Optional;

public interface IBranchService  extends IGeneralService<BranchDTO> {
    List<String> findAllLocationBranch();
    List<BranchDTO> findBranchByLocation(String location);
    List<BranchDTO> getBranchByMovie(Integer movieId);
    List<BranchDTO> getBranchByStartDate(Integer movieId, String startDate);
    List<BranchDTO> findAllBranchesDTO();


    List<Branch> getAllBranchs();
    Optional<Branch> getBranchById(Integer id);
    Branch saveBranch(Branch cinema);
    Branch updateBranch(Branch cinema);
    void deleteBranch(Integer id);

    List<Branch> getBranchsByCity(String city);
    Optional<Branch> getBranchByName(String name);
    Optional<Branch> getBranchByPhone(String phone);

    // Branch with rooms
    List<Branch> getBranchsWithRooms();
    Long getRoomCount(Integer cinemaId);
    Long getTotalCapacity(Integer cinemaId);

    // Search and filter
    List<Branch> searchBranchsByAddress(String address);
    boolean isBranchNameUnique(String name);
    boolean isBranchPhoneUnique(String phone);

    // Activate/Deactivate
    void activateBranch(Integer id);
    void deactivateBranch(Integer id);

    // Validation
    boolean existsById(Integer id);
    boolean isActiveBranch(Integer id);
}


