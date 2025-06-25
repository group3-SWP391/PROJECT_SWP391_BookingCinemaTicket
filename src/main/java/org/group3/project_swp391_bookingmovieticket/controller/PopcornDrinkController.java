package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.PopcornDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_LOGIN_DTO;

@Controller
public class PopcornDrinkController {

    @Autowired
    private PopcornDrinkService popcornDrinkService;

    @GetMapping("/pick-popcorn-drink")
    public String pickPopcornDrink(Model model) {
        model.addAttribute("allPopcornDrink", popcornDrinkService.findAll());
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        return "popcorn_drink_booking";
    }

}
