package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.RoomService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
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

    @GetMapping("/{scheduleId}")
    public String displayRoomAndSeat(@PathVariable("scheduleId") Integer scheduleId,
                                     Model model, HttpServletRequest request) {
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
        return "seat_booking";
    }

}
