package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public String displayMovieAll() {
        return "home";
    }

    @GetMapping("/search")
    public String displayAllMovies(@RequestParam(value = "movieNameSearch", required = false) String movieNameSearch,
                                   Model model) {
        if (movieNameSearch == null || movieNameSearch.trim().isEmpty()) {
            model.addAttribute("movieAll", movieService.findAll());
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        } else {
            model.addAttribute("movieAll", movieService.findByMovieName(movieNameSearch));
        }
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

}
