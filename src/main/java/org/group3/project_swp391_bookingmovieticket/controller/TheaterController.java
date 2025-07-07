package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Room;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.RoomRepository;
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
    private RoomRepository roomRepository;

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
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            @RequestParam(value = "roomId", required = false) String roomIdRaw,
            @RequestParam(value = "startHour", required = false) Integer startHour
    ) {
        Logger logger = LoggerFactory.getLogger(TheaterController.class);
        logger.info("▶️ Fetching theater detail for branchId: {}", branchId);

        // Lấy rap detail
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            logger.error("⛔ Branch with id {} not found", branchId);
            return "redirect:/select-theater";
        }
        model.addAttribute("branch", branch);

        Integer roomId = null;
        if (roomIdRaw != null && !roomIdRaw.isBlank()) {
            try {
                roomId = Integer.valueOf(roomIdRaw);
            } catch (NumberFormatException e) {
                logger.warn("⚠️ Invalid roomId received: '{}'", roomIdRaw);
            }
        }
        logger.info("📌 Filter roomId: {}", roomId);

        // Ngày được chọn (mặc định là hôm nay nếu không chọn)
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        model.addAttribute("selectedDate", selectedDate);

        // Lấy danh sách lịch chiếu tại rạp
        List<Schedule> schedules = scheduleRepository.findByBranchId(branchId);
        model.addAttribute("schedules", schedules != null ? schedules : List.of());

        if (schedules != null && !schedules.isEmpty()) {
            LocalDate finalDate = selectedDate;
            LocalDateTime now = LocalDateTime.now();

            Integer finalRoomId = roomId;
            List<Schedule> filteredSchedules = schedules.stream()
                    // van con chiéu
                    .filter(s -> !s.getEndDate().isBefore(finalDate))
                    // đã bắt đầu chiếu
                    .filter(s -> !s.getStartDate().isAfter(finalDate))
                    .filter(s -> {
                        LocalDateTime showTime = LocalDateTime.of(finalDate, s.getStartTime());
                        return showTime.isAfter(now); // chưa chiếu
                    })
                    .filter(s -> finalRoomId == null || s.getRoom().getId().equals(finalRoomId)) // lọc theo phòng
                    .filter(s -> startHour == null || s.getStartTime().getHour() == startHour) // lọc theo giờ
                    .collect(Collectors.toList());

            Map<Integer, Map<Integer, Integer>> availableSeatsMap = new HashMap<>();
            Map<Integer, Integer> capacityMap = new HashMap<>();

            for (Schedule schedule : filteredSchedules) {
                int room = schedule.getRoom().getId();
                int scheduleId = schedule.getId();

                var seats = seatService.getSeatsByScheduleIdAndUserId(scheduleId, 0);

                long available = seats.stream()
                        .filter(seat -> Boolean.TRUE.equals(seat.getIsActive())
                                && !seat.isOccupied()
                                && !seat.isChecked())
                        .count();

                int capacity = schedule.getRoom().getCapacity();
                availableSeatsMap.computeIfAbsent(room, k -> new HashMap<>())
                        .put(scheduleId, (int) available);
                capacityMap.put(room, capacity);
            }

            // Nhóm theo phim
            Map<String, List<Schedule>> groupedSchedules = filteredSchedules.stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getMovie().getName(),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            model.addAttribute("availableSeatsMap", availableSeatsMap);
            model.addAttribute("capacityMap", capacityMap);
            model.addAttribute("groupedSchedules", groupedSchedules);
        } else {
            // Nếu ko co lich chieu
            model.addAttribute("availableSeatsMap", new HashMap<>());
            model.addAttribute("capacityMap", new HashMap<>());
            model.addAttribute("groupedSchedules", new HashMap<>());
            logger.info("ℹ️ No schedules available for filtering.");
        }

        // Thêm danh sách phòng để hiển thị dropdown lọc
        List<Room> availableRooms = roomRepository.findByBranchId(branchId);
        model.addAttribute("availableRooms", availableRooms);

        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("selectedStartHour", startHour);

        return "theater-detail";
    }


}
