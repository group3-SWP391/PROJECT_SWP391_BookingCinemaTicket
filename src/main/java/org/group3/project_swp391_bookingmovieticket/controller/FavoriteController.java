package org.group3.project_swp391_bookingmovieticket.controller;


import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam("movieId") Integer movieId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) return "redirect:/login";

        favoriteService.toggleFavorite(user, movieId);
        return "redirect:/movie-details?id=" + movieId;
    }

    @GetMapping("/my-list")
    public String favoriteList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) return "redirect:/login";

        model.addAttribute("favoriteList", favoriteService.getFavorites(user));
        return "favorite-list";
    }
}

