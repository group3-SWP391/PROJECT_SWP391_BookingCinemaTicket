package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.services.IPopcornDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller

public class FoodOrderController {
    @Autowired
    private IPopcornDrinkService iPopcornDrinkService;
    @PostMapping("/foodorder")
    public String showFoodPage(@RequestParam Integer scheduleId,
                                 @RequestParam String selectedSeats,
                                 HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("schedulemovie", scheduleId);
        session.setAttribute("listseat",selectedSeats);

        List<PopcornDrink> popcornDrinkList = iPopcornDrinkService.findAll();
        model.addAttribute("listfood", popcornDrinkList);
        return "employee/food";




    }
}
