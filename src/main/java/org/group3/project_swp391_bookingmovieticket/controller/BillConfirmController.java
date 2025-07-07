package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.IBillService;
import org.group3.project_swp391_bookingmovieticket.services.IScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.ISeatService;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BillConfirmController {

    @Autowired
    private IScheduleService iScheduleService;
    @Autowired
    private ITicketService iTicketService;
    @Autowired
    private IBillService iBillService;
    @Autowired
    private ISeatService iSeatService;

    @PostMapping("/confirm-booking")
    public String confirmBooking(@RequestParam Integer scheduleId,
                                 @RequestParam String seatIds,
                                 @RequestParam Double totalPrice, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session == null){
            model.addAttribute("messageerror", "Quá hạn thời giạn thao tác. Vui lòng thực hiện lại");
            return "employee/error";
        }
        User user = (User)request.getAttribute("userLogin");

        List<Integer> seatIdList = Arrays.stream(seatIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setPrice(totalPrice);
        bill.setCreatedTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        bill = iBillService.save(bill);

        for (Integer seatId : seatIdList) {
            Ticket ticket = new Ticket();

            ticket.setBill(bill);

            ticket.setSeat((Seat)iSeatService.findById(seatId).get());

            ticket.setSchedule((Schedule)iScheduleService.findById(scheduleId).get());

            ticket.setQrImageurl("https://example.com/qr-placeholder.png");

            iTicketService.save(ticket);
            model.addAttribute("message", "Thanh toán thành công.");
        }

        return "employee/success";
    }
}
