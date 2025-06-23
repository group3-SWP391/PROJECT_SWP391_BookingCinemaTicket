package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
import org.group3.project_swp391_bookingmovieticket.services.impl.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/{scheduleId}")
    public String displayRoomAndSeat(@PathVariable("scheduleId") Integer scheduleId,
                                     Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Use false to avoid creating a new session
        User user = (session != null) ? (User) session.getAttribute("userLogin") : null;
        Integer userId = (user != null) ? user.getId() : 0;

        // Get seats, handle userId = 0 appropriately in SeatService
        List<SeatDTO> seats = seatService.getSeatsByScheduleIdAndUserId(scheduleId, userId);

        // Giả sử mỗi hàng có 10 ghế
        int seatsPerRow = 10;
        int totalRows = (int) Math.ceil((double) seats.size() / seatsPerRow);

        // Tạo danh sách ký hiệu hàng: A, B, C, D, ...
        List<String> rowLabels = IntStream.range(0, totalRows)
                .mapToObj(i -> String.valueOf((char) ('A' + i)))
                .toList();

        // Add attributes to model
        model.addAttribute("schedule", scheduleService.getScheduleByScheduleId(scheduleId));
        model.addAttribute("seats", seats);
        model.addAttribute("rowLabels", rowLabels);
        model.addAttribute("seatsPerRow", seatsPerRow);
        model.addAttribute("userDTO", convertUserToDTO(user)); // Use converted user or null
        model.addAttribute("eventId", "movie_event_1"); // Adjust based on Schedule entity
        model.addAttribute("ticketType", "regular"); // Adjust based on Schedule or Seat

        return "seat_booking";
    }

    private UserDTO convertUserToDTO(User user) {
        if (user == null) {
            return new UserDTO(); // Return empty DTO for unauthenticated users
        }

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

    @PostMapping("/api/vouchers/apply")
    public ResponseEntity<?> applyVoucher(
            @RequestParam Integer userId,
            @RequestParam String voucherCode,
            @RequestParam double totalAmount,
            @RequestParam String event,
            @RequestParam String ticketType,
            @RequestParam String userType
    ) {
        try {
            Voucher voucher = voucherService.applyVoucher(userId, voucherCode, totalAmount, event, ticketType, userType);
            return ResponseEntity.ok(Map.of("voucher", voucher));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


}