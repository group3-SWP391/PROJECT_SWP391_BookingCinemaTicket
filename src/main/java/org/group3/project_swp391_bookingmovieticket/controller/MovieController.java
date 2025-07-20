package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserDTO;
import org.group3.project_swp391_bookingmovieticket.service.impl.MovieService;
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
        List<MovieDTO> movies;
        if (categoryName == null || categoryName.trim().isEmpty() || categoryName.equalsIgnoreCase("movieAll")) {
            movies = movieService.findAll();
        } else {
            movies = movieService.getMovieByCategory(categoryName);
        }
        movies.forEach(movie -> System.out.println("Trailer URL for " + movie.getName() + ": " + movie.getTrailerURL()));
        return movies;
    }

    @GetMapping("/movie-category")
    public String displayMovieCategory(@RequestParam(value = "categoryName", required = false) String categoryName,
                                       Model model) {
        if (categoryName == null || categoryName.trim().isEmpty() || categoryName.equalsIgnoreCase("Tất cả")) {
            model.addAttribute("movieByCategory", movieService.findAll());
        } else {
            model.addAttribute("movieByCategory", movieService.getMovieByCategory(categoryName));
        }
        model.addAttribute("movieAll", movieService.findAll());
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("userDTO", new UserDTO());
        return "movie_category";
    }



}