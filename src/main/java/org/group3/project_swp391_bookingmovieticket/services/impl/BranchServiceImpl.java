package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BranchServiceImpl implements IBranchService {
    
    @Autowired
    private IBranchRepository branchRepository;
    
    @Autowired
    private IRoomRepository roomRepository;
    
    @Override
    public List<Branch> getAllBranchs() {
        return branchRepository.findAll();
    }
    
    @Override
    public Optional<Branch> getBranchById(Integer id) {
        return branchRepository.findById(id);
    }
    
    @Override
    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }
    
    @Override
    public Branch updateBranch(Branch branch) {
        return branchRepository.save(branch);
    }
    
    @Override
    public void deleteBranch(Integer id) {
        // Soft delete - deactivate instead of hard delete
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }
    
    @Override
    public List<Branch> getBranchsByCity(String city) {
        return branchRepository.findByCity(city);
    }
    
    @Override
    public Optional<Branch> getBranchByName(String name) {
        return branchRepository.findByName(name);
    }
    
    @Override
    public Optional<Branch> getBranchByPhone(String phone) {
        return branchRepository.findByPhoneNo(phone);
    }
    

    @Override
    public List<Branch> getBranchsWithRooms() {
        return branchRepository.findBranchsWithRooms();
    }
    
    @Override
    public Long getRoomCount(Integer branchId) {
        return branchRepository.countActiveRoomsByBranchId(branchId);
    }
    
    @Override
    public Long getTotalCapacity(Integer branchId) {
        Long capacity = roomRepository.getTotalCapacityByBranchId(branchId);
        return capacity != null ? capacity : 0L;
    }
    
    @Override
    public List<Branch> searchBranchsByAddress(String address) {
        return branchRepository.findByLocationContainingIgnoreCase(address);
    }
    
    @Override
    public boolean isBranchNameUnique(String name) {
        return branchRepository.findByName(name).isEmpty();
    }
    
    @Override
    public boolean isBranchPhoneUnique(String phone) {
        return branchRepository.findByPhoneNo(phone).isPresent();
    }
    

    @Override
    public void activateBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }
    
    @Override
    public void deactivateBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isPresent()) {
            Branch existingBranch = branch.get();
            branchRepository.save(existingBranch);
        }
    }
    
    @Override
    public boolean existsById(Integer id) {
        return branchRepository.existsById(id);
    }
    
    @Override
    public boolean isActiveBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        return branch.isPresent();
    }
} 