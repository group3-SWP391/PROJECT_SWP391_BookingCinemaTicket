package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping()
    public String index(Model model, HttpSession session) {
//        String phone = (String) session.getAttribute("phone");
//        if (phone == null) {
//            return "redirect:/login";
//        }

        User user =(User) session.getAttribute("userLogin");
        if (user == null) {
            System.out.println("user failed in");
            return "redirect:/login";
        } else {
            System.out.println("user logged in");
        }

        Double totalRevenue = billRepository.sumTotalPrice();
        model.addAttribute("revenue", totalRevenue != null ? totalRevenue : 0.0);
        long totalTickets = billRepository.countTicketsSold();
        long totalMovies = movieRepository.count();
        long totalSchedules = scheduleRepository.count();

        model.addAttribute("revenue", totalRevenue);
        model.addAttribute("tickets", totalTickets);
        model.addAttribute("movieCount", totalMovies);
        model.addAttribute("scheduleCount", totalSchedules);

        return "manager_dashboard";
    }
}

//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/dashboard")
//public class ManagerDashboardController {
//
//    @GetMapping
//    public String index() {
//        return "manager_dashboard";
//    }
//}
