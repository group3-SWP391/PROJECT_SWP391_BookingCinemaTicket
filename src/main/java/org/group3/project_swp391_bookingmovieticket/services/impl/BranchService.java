package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private IBranchRepository branchRepository;

    @Override
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public Optional<Branch> findById(Integer id) {
        return branchRepository.findById(id);
    }

    @Override
    public void save(Branch entity) {
        branchRepository.save(entity);
    }

    @Override
    public void update(Branch entity) {
        branchRepository.save(entity);
    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void delete(Integer id) {
        branchRepository.deleteById(id);
    }

    @Override
    public List<Branch> findByDiaChi(String diaChi) {
        return branchRepository.findByDiaChi(diaChi);
    }
    @Override
    public List<BranchDTO> findAllBranchesDTO() {
        return branchRepository.findAll().stream()
                .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getDiaChi()))
                .collect(Collectors.toList());
    }
}