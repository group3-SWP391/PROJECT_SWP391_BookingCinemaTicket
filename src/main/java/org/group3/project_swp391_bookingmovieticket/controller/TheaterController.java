package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TheaterController {

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private SeatService seatService;

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
    public String theaterDetail(
            Model model,
            @RequestParam("branchId") Integer branchId,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {

        Logger logger = LoggerFactory.getLogger(TheaterController.class);
        logger.info("Fetching theater detail for branchId: {}", branchId);

        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            logger.error("Branch with id {} not found", branchId);
            return "redirect:/select-theater";
        }
        model.addAttribute("branch", branch);

        // Nếu chưa chọn ngày thì lấy ngày hôm nay
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        model.addAttribute("selectedDate", selectedDate);

        // Lấy tất cả lịch chiếu tại rạp
        List<Schedule> schedules = scheduleRepository.findByBranchId(branchId);
        model.addAttribute("schedules", schedules != null ? schedules : List.of());

        if (schedules != null && !schedules.isEmpty()) {
            LocalDate finalDate = selectedDate; // dùng cho lambda
            LocalDateTime now = LocalDateTime.now();

            List<Schedule> filteredSchedules = schedules.stream()
                    .filter(s -> !s.getEndDate().isBefore(finalDate))     // chưa hết chiếu
                    .filter(s -> !s.getStartDate().isAfter(finalDate))    // đã bắt đầu
                    .filter(s -> {
                        LocalDateTime showDateTime = LocalDateTime.of(finalDate, s.getStartTime());
                        return showDateTime.isAfter(now); // chỉ lấy suất chưa chiếu
                    })
                    .collect(Collectors.toList());

            // Tính ghế trống và sức chứa theo từng lịch chiếu
            Map<Integer, Map<Integer, Integer>> availableSeatsMap = new HashMap<>();
            Map<Integer, Integer> capacityMap = new HashMap<>();

            for (Schedule schedule : filteredSchedules) {
                int roomId = schedule.getRoom().getId();
                int scheduleId = schedule.getId();

                var seats = seatService.getSeatsByScheduleIdAndUserId(scheduleId, 0); // userId = 0 để lấy toàn bộ

                long available = seats.stream()
                        .filter(seat -> Boolean.TRUE.equals(seat.getIsActive())
                                && !seat.isOccupied()
                                && !seat.isChecked())
                        .count();

                int capacity = schedule.getRoom().getCapacity();

                availableSeatsMap.computeIfAbsent(roomId, k -> new HashMap<>())
                        .put(scheduleId, (int) available);
                capacityMap.put(roomId, capacity);
            }

            model.addAttribute("availableSeatsMap", availableSeatsMap);
            model.addAttribute("capacityMap", capacityMap);

            // Nhóm theo tên phim
            Map<String, List<Schedule>> groupedSchedules = filteredSchedules.stream()
                    .collect(Collectors.groupingBy(
                            schedule -> schedule.getMovie().getName(),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));
            model.addAttribute("groupedSchedules", groupedSchedules);

        } else {
            model.addAttribute("availableSeatsMap", new HashMap<>());
            model.addAttribute("capacityMap", new HashMap<>());
            model.addAttribute("groupedSchedules", new HashMap<>());
        }

        return "theater-detail";
    }
}
