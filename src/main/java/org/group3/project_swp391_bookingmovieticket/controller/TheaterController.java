package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TheaterController {

    @Autowired
    private IBranchRepository branchRepository;

    @GetMapping("/select-theater")
    public String selectTheater(Model model, @RequestParam(value = "name", required = false) String selectedName) {
        // Lấy danh sách name duy nhất
        List<String> branchNames = branchRepository.findAll()
                .stream()
                .map(Branch::getName)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("branchNames", branchNames);

        // Nếu đã chọn name, lấy danh sách diaChi tương ứng
        if (selectedName != null && !selectedName.isEmpty()) {
            List<BranchDTO> addresses = branchRepository.findByName(selectedName)
                    .stream()
                    .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getDiaChi()))
                    .collect(Collectors.toList());
            model.addAttribute("addresses", addresses);
        }

        return "select_theater";
    }

    @GetMapping("/api/addresses")
    @ResponseBody
    public List<BranchDTO> getAddressesByName(@RequestParam("name") String name) {
        return branchRepository.findByName(name)
                .stream()
                .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getDiaChi()))
                .collect(Collectors.toList());
    }
}