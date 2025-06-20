package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {
    @Autowired
    private IBranchRepository iBranchRepository;
    @Override
    public List findAll() {
        return iBranchRepository.findAll();
    }

    @Override
    public Optional findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }
}
