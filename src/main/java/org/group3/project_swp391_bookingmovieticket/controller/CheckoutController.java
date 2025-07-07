package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.Seat;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CheckoutController {
    @Autowired
    private ITicketService iTicketService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/checkout")
    public String handleCheckout(@RequestParam Integer scheduleId,
                                 @RequestParam String selectedSeats,
                                 HttpServletRequest request) {

        HttpSession session = request.getSession();


        List<Long> seatIds = new ArrayList<>();
        String[] seatIdStrings = selectedSeats.split(",");

        for (String seatIdStr : seatIdStrings) {
            seatIds.add(Long.parseLong(seatIdStr));
        }

        Optional<Schedule> optionalSchedule = scheduleService.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy lịch chiếu!");
            return "redirect:/"; // hoặc về lại trang chọn lịch
        }

        // Lấy danh sách ghế
        List<Seat> seats = seatService.findSeatsByIds(seatIds);

        // Lấy danh sách ghế đã đặt
        HashSet<Integer> bookedSeatIds = iTicketService.findBookedSeatIdsBySchedule(scheduleId);

        // Nếu có bất kỳ ghế nào trùng thì báo lỗi
        if (seatIds.stream().map(Long::intValue).anyMatch(bookedSeatIds::contains)) {
            session.setAttribute("error", "Ghế đã có người đặt!");
            return "employee/seat_selection";
        }

        // Tính tổng tiền
        double basePrice = optionalSchedule.get().getPrice();
        double total = 0;
        for (Seat seat : seats) {
            total += basePrice + (seat.getIsVip() ? 20000 : 0);
        }

        // Lưu vào session để chuyển sang bước thanh toán QR
        session.setAttribute("scheduleId", scheduleId);
        session.setAttribute("seatIds", selectedSeats);
        session.setAttribute("totalPrice", total);

        return "redirect:/payment/qr";
    }
}
