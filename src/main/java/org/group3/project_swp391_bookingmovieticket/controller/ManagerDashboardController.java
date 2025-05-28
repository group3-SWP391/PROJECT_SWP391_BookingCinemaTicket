package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class ManagerDashboardController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IBillRepository billRepository;

    @GetMapping
    public String index(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("user", user);
        return "dashboard/index";
    }

    @GetMapping("/schedules")
    public String viewSchedules(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "dashboard/schedules";
    }

    @GetMapping("/movies")
    public String viewMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "dashboard/movies";
    }

    @GetMapping("/statistics")
    public String viewStatistics(Model model) {
        double totalRevenue = billRepository.sumTotalPrice();
        long totalTickets = billRepository.countTicketsSold();
        model.addAttribute("revenue", totalRevenue);
        model.addAttribute("tickets", totalTickets);
        return "dashboard/statistics";
    }
}


