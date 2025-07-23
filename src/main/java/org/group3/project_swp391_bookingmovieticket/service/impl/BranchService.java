package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.group3.project_swp391_bookingmovieticket.repository.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {
    @Autowired
    private IBranchRepository branchRepository;
    @Override
    public List findAll() {
        return branchRepository.findAll();
    }

    @Override
    public Optional findById(Integer id) {
        return branchRepository.findById(id);
    }

    @Override
    public void update(Branch branch) {

    }



    @Override
    public void remove(Integer id) {

    }
}
