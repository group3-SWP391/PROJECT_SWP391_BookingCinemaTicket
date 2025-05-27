package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
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

    // Trang chủ
    @GetMapping
    public String displayHomePage(Model model) {
        model.addAttribute("movieAll", movieService.findAll());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    // Trang About
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "about";
    }

    // Trang Blog Category
    @GetMapping("/blog-category")
    public String blogCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_category";
    }

    // Trang Blog Single
    @GetMapping("/blog-single")
    public String blogSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_single";
    }

    // Trang Booking Type
    @GetMapping("/booking-type")
    public String bookingType(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "booking_type";
    }

    // Trang Confirmation Screen
    @GetMapping("/confirmation-screen")
    public String confirmationScreen(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "confirmation_screen";
    }

    // Trang Contact
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "contact";
    }

    // Trang Event Category
    @GetMapping("/event-category")
    public String eventCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_category";
    }

    // Trang Event Single
    @GetMapping("/event-single")
    public String eventSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_single";
    }

    // Trang Gallery
    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "gallery";
    }

    // Trang Movie Booking
    @GetMapping("/movie-booking")
    public String movieBooking(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_booking";
    }

    // Trang Movie Category
    @GetMapping("/movie-category")
    public String movieCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_category";
    }

    // Trang Movie Single
    @GetMapping("/movie-single")
    public String movieSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single";
    }

    // Trang Movie Single Second
    @GetMapping("/movie-single-second")
    public String movieSingleSecond(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single_second";
    }

    // Trang Seat Booking
    @GetMapping("/seat-booking")
    public String seatBooking(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "seat_booking";
    }
}