package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
        } else {
            model.addAttribute("movieAll", movieService.findByMovieName(movieNameSearch));
        }
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("movieByCategory", movieService.findAll());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    @GetMapping(value = "/now-showing", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MovieDTO> getNowShowingMovies() {
        return movieService.getNowShowingMovies();
    }

    @GetMapping(value = "/comming-soon", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MovieDTO> getCommingSoonMovies() {
        return movieService.getCommingSoonMovies();
    }


    @GetMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MovieDTO> getMoviesByCategory(@RequestParam(value = "categoryName", required = false) String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty() || categoryName.equalsIgnoreCase("movieAll")) {
            return movieService.findAll();
        } else {
            return movieService.getMovieByCategory(categoryName);
        }
    }

}
