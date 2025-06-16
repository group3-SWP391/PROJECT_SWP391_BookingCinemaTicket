package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.constant.CommonConst;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/confirmation_screen")
public class BillController {
    @GetMapping
    public String confirmationScreen(Model model) {
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        return "confirmation_screen";
    }
}
