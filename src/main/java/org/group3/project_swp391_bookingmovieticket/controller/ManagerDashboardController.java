package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.dtos.MovieRevenueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.time.Year;
import java.time.YearMonth;

@Controller
@RequestMapping("/manager")
public class ManagerDashboardController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private IBillRepository billRepository;

    @GetMapping("/dashboard")
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return "redirect:/login";
        }

        // Calculate monthly revenue for overall dashboard
        List<Double> monthlyRevenue = new ArrayList<>();
        int year = Year.now().getValue();
        for (int month = 1; month <= 12; month++) {
            Double revenue = billRepository.getRevenueForMonth(month, year);
            monthlyRevenue.add(revenue != null ? revenue : 0.0);
        }

        // Calculate total revenue by summing individual movie revenues
        double totalRevenue = 0.0;
        List<org.group3.project_swp391_bookingmovieticket.entities.Movie> allMovies = movieRepository.findAll();
        for (org.group3.project_swp391_bookingmovieticket.entities.Movie movie : allMovies) {
            Double movieRevenue = billRepository.getRevenueByMovie(movie.getId());
            if (movieRevenue != null) {
                totalRevenue += movieRevenue;
            }
        }

        // Debug: Check if movies exist
        System.out.println("Total movies found: " + allMovies.size());
        for (org.group3.project_swp391_bookingmovieticket.entities.Movie movie : allMovies) {
            System.out.println("Movie: " + movie.getName() + " (ID: " + movie.getId() + ")");
        }

        // Create MovieRevenueDTO list with error handling
        List<MovieRevenueDTO> movieRevenueList = new ArrayList<>();
        try {
            movieRevenueList = allMovies.stream()
                    .map(m -> {
                        try {
                            System.out.println("Processing movie: " + m.getName());

                            // Get total revenue with error handling
                            Double revenue = null;
                            try {
                                revenue = billRepository.getRevenueByMovie(m.getId());
                                System.out.println("Revenue for " + m.getName() + ": " + revenue);
                            } catch (Exception e) {
                                System.err.println("Error getting revenue for movie " + m.getName() + ": " + e.getMessage());
                                revenue = 0.0;
                            }

                            // Get tickets sold with error handling
                            Long tickets = null;
                            try {
                                tickets = billRepository.getTicketsSoldByMovie(m.getId());
                                System.out.println("Tickets for " + m.getName() + ": " + tickets);
                            } catch (Exception e) {
                                System.err.println("Error getting tickets for movie " + m.getName() + ": " + e.getMessage());
                                tickets = 0L;
                            }

                            // Get last show date with error handling
                            java.sql.Date lastShow = null;
                            try {
                                lastShow = scheduleRepository.getLastShowDateByMovie(m.getId());
                                System.out.println("Last show for " + m.getName() + ": " + lastShow);
                            } catch (Exception e) {
                                System.err.println("Error getting last show for movie " + m.getName() + ": " + e.getMessage());
                            }

                            // Calculate monthly revenue for last 12 months
                            List<Double> movieMonthlyRevenue = new ArrayList<>();
                            for (int i = 0; i < 12; i++) {
                                try {
                                    YearMonth yearMonth = YearMonth.now().minusMonths(i);
                                    Double monthRevenue = billRepository.getMonthlyRevenueByMovie(
                                            m.getId(),
                                            yearMonth.getMonthValue(),
                                            yearMonth.getYear()
                                    );
                                    movieMonthlyRevenue.add(0, monthRevenue != null ? monthRevenue : 0.0);
                                } catch (Exception e) {
                                    System.err.println("Error getting monthly revenue for movie " + m.getName() + ": " + e.getMessage());
                                    movieMonthlyRevenue.add(0, 0.0);
                                }
                            }
                            System.out.println("Monthly revenue for " + m.getName() + ": " + movieMonthlyRevenue);

                            return new MovieRevenueDTO(
                                    m.getName(),
                                    revenue != null ? revenue : 0.0,
                                    tickets != null ? tickets.intValue() : 0,
                                    lastShow != null ? lastShow.toString() : "No shows",
                                    m.getId(),
                                    movieMonthlyRevenue
                            );
                        } catch (Exception e) {
                            System.err.println("Error processing movie " + m.getName() + ": " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null) // Remove null entries
                    .toList();
        } catch (Exception e) {
            System.err.println("Error creating movie revenue list: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Final Movie Revenue List size: " + movieRevenueList.size());

        // Convert movieRevenueList to JSON string for JavaScript
        String movieRevenueListJson = "[]";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            movieRevenueListJson = objectMapper.writeValueAsString(movieRevenueList);
            System.out.println("MovieRevenueList JSON: " + movieRevenueListJson);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting movieRevenueList to JSON: " + e.getMessage());
        }

        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("revenue", totalRevenue);
        model.addAttribute("tickets", billRepository.countTicketsSold());
        model.addAttribute("movieCount", movieRepository.count());
        model.addAttribute("scheduleCount", scheduleRepository.count());
        model.addAttribute("movies", allMovies);
        model.addAttribute("movieRevenueList", movieRevenueList);
        model.addAttribute("movieRevenueListJson", movieRevenueListJson);
        model.addAttribute("movieRevenueListRaw", movieRevenueListJson.replace("\"", "\\\""));
        model.addAttribute("content", "manager/dashboard");

        return "manager/layout";
    }
}