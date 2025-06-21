package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TheaterController {

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private IScheduleRepository scheduleRepository;

    @GetMapping("/select-theater")
    public String selectTheater(Model model,
                                @RequestParam(value = "name", required = false) String selectedName,
                                @RequestParam(value = "branchId", required = false) Integer branchId) {
        List<String> branchNames = branchRepository.findAll()
                .stream()
                .map(Branch::getName)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("branchNames", branchNames);

        if (selectedName != null && !selectedName.isEmpty()) {
            List<BranchDTO> addresses = branchRepository.findByName(selectedName)
                    .stream()
                    .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getDiaChi()))
                    .collect(Collectors.toList());
            model.addAttribute("addresses", addresses);
        }
        if (branchId != null) {
            return "redirect:/theater-detail?branchId=" + branchId;
        }
        return "select_theater";
    }

    @GetMapping("/theater-detail")
    public String theaterDetail(Model model, @RequestParam("branchId") Integer branchId) {
        Logger logger = LoggerFactory.getLogger(TheaterController.class);
        logger.info("Fetching theater detail for branchId: {}", branchId);

        Branch branch = branchRepository.findById(branchId).orElse(null);
        System.out.println("branch: " + branch);
        if (branch == null) {
            logger.error("Branch with id {} not found", branchId);
            return "redirect:/select-theater";
        }
        model.addAttribute("branch", branch);

        List<Schedule> schedules = scheduleRepository.findByBranchId(branchId);
        System.out.println("schedules: " + schedules);
        if (schedules == null || schedules.isEmpty()) {
            logger.warn("No schedules found for branchId: {}", branchId);
        }
        model.addAttribute("schedules", schedules != null ? schedules : List.of());

        return "theater-detail";
    }
}