package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.EventService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public String displayHomePage(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("categoryAll", movieService.getMovieCategories());
        request.getSession().setAttribute("eventImage", eventService.findEventValid());
        model.addAttribute("movieHighView", movieService.findMovieByViewDesc());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    @GetMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "contact";
    }

    @GetMapping("/event")
    public String displayEventPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_single";
    }

}
