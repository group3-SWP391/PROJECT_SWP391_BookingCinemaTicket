package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.repositories.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class StaffDashboardController {

    private final IMovieRepository movieRepository;
    private final IScheduleRepository scheduleRepository;
    private final IBillRepository billRepository;

    @Autowired
    public StaffDashboardController(IMovieRepository movieRepository,
                                    IScheduleRepository scheduleRepository,
                                    IBillRepository billRepository) {
        this.movieRepository = movieRepository;
        this.scheduleRepository = scheduleRepository;
        this.billRepository = billRepository;
    }

    @GetMapping()
    public String showDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) return "redirect:/login";

        model.addAttribute("revenue", billRepository.sumTotalPrice() != null ? billRepository.sumTotalPrice() : 0.0);
        model.addAttribute("tickets", billRepository.countTicketsSold());
        model.addAttribute("movieCount", movieRepository.count());
        model.addAttribute("scheduleCount", scheduleRepository.count());

        return "staff/staff_dashboard";
    }

    @GetMapping("/movies")
    public String listMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "staff/movies";
    }

    @GetMapping("/movies/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "staff/add_movie";
    }

    @PostMapping("/movies/add")
    public String addMovie(@ModelAttribute("movie") Movie movie) {
        movieRepository.save(movie);
        return "redirect:/dashboard/movies";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable("id") Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) return "redirect:/dashboard/movies";
        model.addAttribute("movie", movie);
        return "staff/edit_movie";
    }

    @PostMapping("/movies/edit")
    public String editMovie(@ModelAttribute("movie") Movie movie) {
        movieRepository.save(movie);
        return "redirect:/dashboard/movies";
    }

    @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieRepository.deleteById(id);
        return "redirect:/dashboard/movies";
    }

    @GetMapping("/schedules")
    public String viewSchedules(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "staff/schedule";
    }

    @GetMapping("/movies/{movieId}/schedules")
    public String viewSchedulesByMovie(@PathVariable Long movieId, Model model) {
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        model.addAttribute("schedules", schedules);
        return "staff/schedule";  // Tên file html hiển thị lịch chiếu
    }
}