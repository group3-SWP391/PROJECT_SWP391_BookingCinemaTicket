package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.constant.CommonConst;
import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.services.impl.BillService;
import org.group3.project_swp391_bookingmovieticket.services.impl.PaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private BillService billService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/create_bill")
    public String createBill(@RequestParam("orderCode") long orderCode,
                             @RequestParam("status") String status,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {

        BookingRequestDTO dto = (BookingRequestDTO) request.getSession().getAttribute("bookingRequestDTO");

        if ("PAID".equals(status) && request.getSession().getAttribute("billCreated") == null) {
            billService.createNewBill(dto);
            paymentLinkService.updateStatusByOrderCode(orderCode, status);
            request.getSession().setAttribute("billCreated", true);
        }
        redirectAttributes.addAttribute("orderCode", orderCode);
        return "redirect:/bill/confirmation_screen";
    }

    @GetMapping("/confirmation_screen")
    public String confirmationScreen(@RequestParam("orderCode") long orderCode,
                                     HttpServletRequest request,
                                     Model model) {
        BookingRequestDTO bookingRequestDTO = (BookingRequestDTO) request.getSession().getAttribute("bookingRequestDTO");
        if (bookingRequestDTO == null) {
            return "redirect:/home";
        }
        Optional<Schedule> scheduleOpt = scheduleService.findById(bookingRequestDTO.getScheduleId());
        if (scheduleOpt.isPresent()) {
            model.addAttribute("scheduleOrder", scheduleOpt.get());
        }
        model.addAttribute("paymentLink", paymentLinkService.findByOrderCode(orderCode));
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        return "confirmation_screen";
    }

    @GetMapping("/cancel_screen")
    public String cancelScreen(@RequestParam("orderCode") long orderCode,
                               @RequestParam("status") String status,
                               Model model) {
        paymentLinkService.updateStatusByOrderCode(orderCode, status);
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        return "cancel_screen";
    }
}
