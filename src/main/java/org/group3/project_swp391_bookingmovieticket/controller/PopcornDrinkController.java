package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.repositories.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_LOGIN_DTO;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_REGISTER_DTO;

@Controller
public class PopcornDrinkController {

    @Autowired
    private PopcornDrinkService popcornDrinkService;

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/pick-popcorn-drink")
    public String pickPopcornDrink(@RequestParam(value = "userId", required = false) Integer userId,
                                   @RequestParam("scheduleId") Integer scheduleId,
                                   @RequestParam("listSeatId") List<Integer> listSeatId,
                                   @RequestParam("totalPrice") Integer totalPrice,
                                   @RequestParam("movieId") Integer movieId,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "4") int size,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        for (Integer seatId : listSeatId) {
            // Kiểm tra ghế đã bị người khác đặt chưa
            boolean isAlreadyBooked = paymentLinkService.existsBySchedule_IdAndSeatListAndStatus(
                    scheduleId,
                    seatService.findSeatNameById(seatId));
            System.out.println(isAlreadyBooked + "pickPopcornDrink kiểm tra ghế đã được đặt chưa");
            if (isAlreadyBooked) {
                System.out.println(isAlreadyBooked + "pickPopcornDrink kiểm tra ghế đã được đặt chưa 2");
                redirectAttributes.addFlashAttribute("errorMessageDuplicate", "Someone was quicker and booked seat " + seatId + ", please choose again!");
                return "redirect:/booking/"  + scheduleId;
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("bookingRequest", new BookingRequestDTO(userId, scheduleId, listSeatId, totalPrice, movieId));
        model.addAttribute("listSeatChoose", seatService.findSeatNamesByIdList(listSeatId));
        model.addAttribute("totalPriceSeat", totalPrice);
        model.addAttribute("userId", userId);
        model.addAttribute("listSeatId", listSeatId);
        model.addAttribute("allPopcornDrink", popcornDrinkService.findAllPagination(pageable));
        model.addAttribute("schedule", scheduleService.findById(scheduleId));
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "popcorn_drink_booking";
    }
}
