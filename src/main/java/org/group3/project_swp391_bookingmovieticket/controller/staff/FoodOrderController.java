package org.group3.project_swp391_bookingmovieticket.controller.staff;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.service.IPopcornDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller

public class FoodOrderController {
    @Autowired
    private IPopcornDrinkService popcornDrinkService;
    @PostMapping("/foodorder")
    public String showFoodPage(@RequestParam Integer scheduleId,
                                 @RequestParam String selectedSeats,
                                 HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("schedulemovie", scheduleId);
        session.setAttribute("listseat",selectedSeats);

        List<PopcornDrink> popcornDrinkList = popcornDrinkService.getPopcornDrink(0);
        model.addAttribute("listfood", popcornDrinkList);
        return "employee/food";




    }
}
