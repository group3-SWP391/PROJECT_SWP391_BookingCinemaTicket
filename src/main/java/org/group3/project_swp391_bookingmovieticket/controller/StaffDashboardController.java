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

    // Danh sách tất cả lịch chiếu
    @GetMapping("/schedules")
    public String viewSchedules(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "staff/schedule";
    }

    // Danh sách lịch chiếu theo phim
    @GetMapping("/movies/{movieId}/schedules")
    public String viewSchedulesByMovie(@PathVariable Long movieId, Model model) {
        List<Schedule> schedules = scheduleRepository.findByMovieId(movieId);
        model.addAttribute("schedules", schedules);
        model.addAttribute("movie", movieRepository.findById(movieId).orElse(null));
        return "staff/schedule";
    }

    // Form thêm lịch chiếu mới (liên kết với movieId nếu có)
    @GetMapping({"/schedules/add", "/movies/{movieId}/schedules/add"})
    public String addScheduleForm(@PathVariable(required = false) Long movieId, Model model) {
        Schedule schedule = new Schedule();

        if (movieId != null) {
            Movie movie = movieRepository.findById(movieId).orElse(null);
            schedule.setMovie(movie);
            model.addAttribute("movie", movie);
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("movies", movieRepository.findAll());
        return "staff/add_schedule";
    }

    // Xử lý thêm lịch chiếu
    @PostMapping("/schedules/add")
    public String addSchedule(@ModelAttribute("schedule") Schedule schedule) {
        // Lấy Movie từ DB để tránh transient object exception
        if (schedule.getMovie() != null && schedule.getMovie().getId() != null) {
            Movie movie = movieRepository.findById(schedule.getMovie().getId()).orElse(null);
            schedule.setMovie(movie);
        } else {
            schedule.setMovie(null);
        }
        schedule.setId(null);
        scheduleRepository.save(schedule);
        return "redirect:/dashboard/schedules";
    }

    // Form sửa lịch chiếu
    @GetMapping("/schedules/edit/{id}")
    public String editScheduleForm(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if (schedule == null) return "redirect:/dashboard/schedules";

        model.addAttribute("schedule", schedule);
        model.addAttribute("movies", movieRepository.findAll());
        return "staff/edit_schedule";
    }

    // Xử lý sửa lịch chiếu
    @PostMapping("/schedules/edit")
    public String editSchedule(@ModelAttribute("schedule") Schedule schedule) {
        // Lấy Movie từ DB để tránh transient object exception
        if (schedule.getMovie() != null && schedule.getMovie().getId() != null) {
            Movie movie = movieRepository.findById(schedule.getMovie().getId()).orElse(null);
            schedule.setMovie(movie);
        } else {
            schedule.setMovie(null);
        }
        scheduleRepository.save(schedule);
        return "redirect:/dashboard/schedules";
    }

    // Xóa lịch chiếu
    @GetMapping("/schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleRepository.deleteById(id);
        return "redirect:/dashboard/schedules";
    }
}
