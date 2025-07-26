package org.group3.project_swp391_bookingmovieticket.controller.customer;


import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam("movieId") Integer movieId,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            redirectAttributes.addFlashAttribute("showLoginModal", true);
            return "redirect:/home";
        }
        favoriteService.toggleFavorite(user, movieId);
        return "redirect:/movie/detail?movieId=" + movieId;
    }
}

