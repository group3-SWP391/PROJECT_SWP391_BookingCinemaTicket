package org.group3.project_swp391_bookingmovieticket.controller.payment;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.PaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.service.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_LOGIN_DTO;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_REGISTER_DTO;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PaymentLinkService paymentLinkService;

    @GetMapping("ticket-detail")
    public String ticketDetail( @RequestParam int movieId,
                                @RequestParam int ticketId,
                                @RequestParam long orderCode,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("userLogin");
        if (user.getRole().getName().equalsIgnoreCase("STAFF")) {
            model.addAttribute("ticketCheck", ticketService.findById(ticketId));
            model.addAttribute("paymentLink", paymentLinkService.findByOrderCode(orderCode));
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "staff-ticket-check";
        }
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "redirect:/movie/detail?movieId=" + movieId;
    }
}
