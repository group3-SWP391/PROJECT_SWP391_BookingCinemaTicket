package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.User;
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
        // Lấy user bằng số điện thoại (phone) thay vì username
        User user = userRepository.findByPhone(principal.getName()).orElse(null);
        model.addAttribute("user", user);

        double totalRevenue = billRepository.sumTotalPrice();
        long totalTickets = billRepository.countTicketsSold();
        long totalMovies = movieRepository.count();
        long totalSchedules = scheduleRepository.count();

        model.addAttribute("revenue", totalRevenue);
        model.addAttribute("tickets", totalTickets);
        model.addAttribute("movieCount", totalMovies);
        model.addAttribute("scheduleCount", totalSchedules);

        return "dashboard/managerdashboard";
    }
}

