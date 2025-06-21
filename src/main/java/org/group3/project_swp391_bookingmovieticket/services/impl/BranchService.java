package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public List<BranchDTO> findBranchByLocation(String locationName) {
        return branchRepository.findByLocation(locationName)
                .stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDTO> getBranchByMovie(Integer movieId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        List<BranchDTO> branchDTOList = new ArrayList<>();
        for (Branch branch : branchRepository.getBranchByMovie(movieId)) {
            BranchDTO branchDTO = modelMapper.map(branch, BranchDTO.class);
            branchDTO.setScheduleList(new ArrayList<>());
            for (Schedule schedule : branch.getScheduleList()) {
                // Chỉ lấy lịch của phim có movieId đúng
                if (schedule.getMovie().getId() != movieId) {
                    continue;
                }
                if (schedule.getStartDate().isAfter(today) ||
                        (schedule.getStartDate().isEqual(today) && schedule.getStartTime().isAfter(now))) {
                    branchDTO.getScheduleList().add(modelMapper.map(schedule, ScheduleDTO.class));
                }
            }
            if (!branchDTO.getScheduleList().isEmpty()) {
                branchDTO.setGroupedScheduleByRoomName(
                        branchDTO.getScheduleList()
                                .stream()
                                .sorted(Comparator.comparing(ScheduleDTO::getStartTime)) // sắp xếp trước
                                .collect(Collectors.groupingBy(schedule -> schedule.getRoom().getRoomType()))
                );
                branchDTOList.add(branchDTO);
            }
        }
        return branchDTOList;
    }

    @Override
    public List<BranchDTO> getBranchByStartDate(Integer movieId, String startDate) {
        if (startDate.equals("allDate")) {
            return getBranchByMovie(movieId);
        } else {
            List<BranchDTO> branchDTOList = new ArrayList<>();
            for (Branch branch : branchRepository.getBranchByStartDate(movieId, startDate)) {
                BranchDTO branchDTO = modelMapper.map(branch, BranchDTO.class);
                branchDTO.setScheduleList(new ArrayList<>());
                for (Schedule schedule : branch.getScheduleList()) {
                    if (schedule.getMovie().getId() != movieId ||
                            !schedule.getStartDate().toString().equals(startDate)) {
                        continue;
                    }
                    branchDTO.getScheduleList().add(modelMapper.map(schedule, ScheduleDTO.class));
                }
                if (!branchDTO.getScheduleList().isEmpty()) {
                    branchDTO.setGroupedScheduleByRoomName(
                            branchDTO.getScheduleList()
                                    .stream()
                                    .sorted(Comparator.comparing(ScheduleDTO::getStartTime))
                                    .collect(Collectors.groupingBy(schedule -> schedule.getRoom().getName()))
                    );
                    branchDTOList.add(branchDTO);
                }
            }
            return branchDTOList;
        }
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
