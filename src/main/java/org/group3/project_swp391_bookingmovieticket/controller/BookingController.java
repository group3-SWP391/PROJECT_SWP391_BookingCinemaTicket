package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SeatService seatService;

    @GetMapping("/{scheduleId}")
    public String displayRoomAndSeat(@PathVariable("scheduleId") Integer scheduleId,
                                     Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userLogin");

        List<SeatDTO> seats = seatService.getSeatsByScheduleIdAndUserId(scheduleId, user.getId());

        // Giả sử mỗi hàng có 10 ghế
        int seatsPerRow = 10;
        int totalRows = (int) Math.ceil((double) seats.size() / seatsPerRow);

        // Tạo danh sách ký hiệu hàng: A, B, C, D, ...
        List<String> rowLabels = IntStream.range(0, totalRows)
                .mapToObj(i -> String.valueOf((char) ('A' + i)))
                .toList();

        model.addAttribute("schedule", scheduleService.getScheduleByScheduleId(scheduleId));
        model.addAttribute("seats", seats);
        model.addAttribute("rowLabels", rowLabels);
        model.addAttribute("seatsPerRow", seatsPerRow);
        model.addAttribute("userDTO", new UserDTO());
        return "seat_booking";
    }

    private UserDTO convertUserToDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setFullname(user.getFullname());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }
}