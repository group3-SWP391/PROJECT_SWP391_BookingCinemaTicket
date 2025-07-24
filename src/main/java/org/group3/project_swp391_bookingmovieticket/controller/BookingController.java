package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.service.impl.RoomService;
import org.group3.project_swp391_bookingmovieticket.service.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.service.impl.SeatService;
import org.group3.project_swp391_bookingmovieticket.service.impl.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_LOGIN_DTO;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_REGISTER_DTO;


@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/{scheduleId}")
    public String displayRoomAndSeat(@PathVariable("scheduleId") Integer scheduleId,
                                     Model model,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userLogin");

        List<SeatDTO> seats = seatService.getSeatsByScheduleIdAndUserId(scheduleId, user.getId());
        long availableSeats = seats.stream()
                .filter(seat -> seat.isActive() && !seat.isOccupied() && !seat.isChecked())
                .count();

        // lấy tổng số ghế / cho số row
        int seatsPerRow = roomService.findRoomByScheduleId(scheduleId).getRowCount();
        int capacity = roomService.findRoomByScheduleId(scheduleId).getCapacity();
        int totalRows = (int) Math.ceil((double) capacity / seatsPerRow);

        // Tạo danh sách ký hiệu hàng: A, B, C, D, ...
        List<String> rowLabels = IntStream.range(0, totalRows)
                .mapToObj(i -> String.valueOf((char) ('A' + i)))
                .toList();

        model.addAttribute("schedule", scheduleService.getScheduleByScheduleId(scheduleId));
        model.addAttribute("totalSeats", capacity);
        model.addAttribute("availableSeats", availableSeats);
        model.addAttribute("seats", seats);
        model.addAttribute("rowLabels", rowLabels);
        model.addAttribute("seatsPerRow", seatsPerRow);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        model.addAttribute("eventId", "movie_event_1");
        model.addAttribute("ticketType", "regular");
        return "seat_booking";
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
