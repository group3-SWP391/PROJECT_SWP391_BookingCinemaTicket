package org.group3.project_swp391_bookingmovieticket.controller.manager;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.group3.project_swp391_bookingmovieticket.dto.MovieRevenueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<org.group3.project_swp391_bookingmovieticket.entity.Movie> allMovies = movieRepository.findAll();
        for (org.group3.project_swp391_bookingmovieticket.entity.Movie movie : allMovies) {
            Double movieRevenue = billRepository.getRevenueByMovie(movie.getId());
            if (movieRevenue != null) {
                totalRevenue += movieRevenue;
            }
        }

        List<MovieRevenueDTO> movieRevenueList = new ArrayList<>();
        try {
            movieRevenueList = allMovies.stream()
                    .map(m -> {
                        try {
                            Double revenue = null;
                            try {
                                revenue = billRepository.getRevenueByMovie(m.getId());
                            } catch (Exception e) {
                                System.err.println("Error getting revenue for movie " + m.getName() + ": " + e.getMessage());
                                revenue = 0.0;
                            }

                            Long tickets = null;
                            try {
                                tickets = billRepository.getTicketsSoldByMovie(m.getId());
                            } catch (Exception e) {
                                System.err.println("Error getting tickets for movie " + m.getName() + ": " + e.getMessage());
                                tickets = 0L;
                            }

                            java.sql.Date lastShow = null;
                            try {
                                lastShow = scheduleRepository.getLastShowDateByMovie(m.getId());
                            } catch (Exception e) {
                                System.err.println("Error getting last show for movie " + m.getName() + ": " + e.getMessage());
                            }

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


        // Convert movieRevenueList to JSON string for JavaScript
        String movieRevenueListJson = "[]";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            movieRevenueListJson = objectMapper.writeValueAsString(movieRevenueList);
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

    @GetMapping("/movie-revenue-breakdown/{movieId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMovieRevenueBreakdown(@PathVariable Integer movieId, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get movie details
            org.group3.project_swp391_bookingmovieticket.entity.Movie movie = movieRepository.findById(movieId).orElse(null);
            if (movie == null) {
                response.put("success", false);
                response.put("message", "Movie not found");
                return ResponseEntity.ok(response);
            }

            // Get detailed breakdown using repository queries
            // Note: You'll need to implement these queries in your repositories
            List<Map<String, Object>> breakdown = billRepository.getMovieRevenueBreakdownByMovieId(movieId);
            
            // Calculate summary statistics
            double totalRevenue = 0.0;
            int totalTickets = 0;
            int totalShows = breakdown.size();
            
            for (Map<String, Object> item : breakdown) {
                Double revenue = (Double) item.get("revenue");
                Long tickets = (Long) item.get("ticketsSold");
                
                if (revenue != null) totalRevenue += revenue;
                if (tickets != null) totalTickets += tickets.intValue();
            }
            
            double averagePerShow = totalShows > 0 ? totalRevenue / totalShows : 0.0;
            
            response.put("success", true);
            response.put("movieName", movie.getName());
            response.put("totalRevenue", totalRevenue);
            response.put("totalTickets", totalTickets);
            response.put("totalShows", totalShows);
            response.put("averagePerShow", averagePerShow);
            response.put("breakdown", breakdown);
            
        } catch (Exception e) {
            System.err.println("Error getting movie revenue breakdown: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving movie revenue breakdown");
        }
        
        return ResponseEntity.ok(response);
    }
}