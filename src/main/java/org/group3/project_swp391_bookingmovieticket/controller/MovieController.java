package org.group3.project_swp391_bookingmovieticket.controller;

<<<<<<< HEAD
import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
=======
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 076b1a6e2f871776af23ba40892fb6e79166c0b4
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
=======
>>>>>>> 076b1a6e2f871776af23ba40892fb6e79166c0b4

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
<<<<<<< HEAD
        } else {
            model.addAttribute("movieAll", movieService.findByMovieName(movieNameSearch));
        }
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("movieByCategory", movieService.findAll());
=======
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        } else {
            model.addAttribute("movieAll", movieService.findByMovieName(movieNameSearch));
        }
>>>>>>> 076b1a6e2f871776af23ba40892fb6e79166c0b4
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

<<<<<<< HEAD
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
        // Log the trailerURL for debugging
        movies.forEach(movie -> System.out.println("Trailer URL for " + movie.getName() + ": " + movie.getTrailerURL()));
        return movies;
    }

    // Phương thức đã được cập nhật để lọc phim theo thể loại
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
=======
}
>>>>>>> 076b1a6e2f871776af23ba40892fb6e79166c0b4
