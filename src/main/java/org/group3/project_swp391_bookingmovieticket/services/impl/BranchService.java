package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<String> findAllLocationBranch() {
        return branchRepository.findAllLocationBranch();
    }

    @Override
    public List<BranchDTO> findBranchByLocation(String locatioName) {
        return branchRepository.findByLocation(locatioName)
                .stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<BranchDTO> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(BranchDTO branchDTO) {

    }

    @Override
    public void remove(Integer id) {

    }
}
