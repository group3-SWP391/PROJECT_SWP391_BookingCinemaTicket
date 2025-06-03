package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.DirectorService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieActorService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
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

    @Autowired
    private MovieActorService movieActorService;

    @Autowired
    private DirectorService directorService;

    @GetMapping
    public String displayMovieAll() {
        return "home";
    }

    @GetMapping("/search")
    public String displayAllMovies(@RequestParam(value = "movieNameSearch", required = false) String movieNameSearch,
                                   Model model) {
        if (movieNameSearch == null || movieNameSearch.trim().isEmpty() || movieNameSearch.trim().equalsIgnoreCase(" ")) {
            model.addAttribute("movieHighView", movieService.findMovieByViewDesc());
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        } else {
            model.addAttribute("movieSearch", movieService.findByMovieName(movieNameSearch));
            model.addAttribute("userDTO", new UserDTO());
        }
        return "movie_search";
    }

    @GetMapping(value = "/detail")
    public String displayMovieDetail(@RequestParam(value = "movieId", required = false) Integer movieId,
                                     Model model) {
        if (movieId == null) {
            model.addAttribute("movieHighView", movieService.findMovieByViewDesc());
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        }
        MovieDTO movieDetail = movieService.findMovieById(movieId);
        model.addAttribute("movieSameCategory", movieService.findMovieSameCategory(movieDetail.getCategories()));
        model.addAttribute("actorByMovie", movieActorService.findAllActorByMovieId(movieId));
        model.addAttribute("directorByMovie", directorService.findDirectorByMovieId(movieId));
        model.addAttribute("movieDetail", movieService.findMovieById(movieId));
        model.addAttribute("userDTO", new UserDTO());
        return "movie_detail";
    }

    @GetMapping(value = "/category/return-view")
    public String getMoviesByCategoryReturnView(@RequestParam(value = "categoryName", required = false)
                                              String categoryName, Model model) {
        model.addAttribute("movieByCategory", movieService.getMovieByCategory(categoryName.trim()));
        model.addAttribute("userDTO",  new UserDTO());
        return "movie_category";
    }

    @GetMapping(value = "/showing")
    public String getMoviesShowing(@RequestParam(value = "status", required = false)
                                              String status, Model model) {
        if (status == null || status.trim().isEmpty()) {
            model.addAttribute("movieHighView", movieService.findMovieByViewDesc());
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        } else {
            if (status.equalsIgnoreCase("now-showing")) {
                model.addAttribute("movieShowing", movieService.findMovieNowShowing());
                model.addAttribute("status", "Now-showing");
            } else if (status.equalsIgnoreCase("coming-soon")) {
                model.addAttribute("movieShowing", movieService.findMovieComingSoon());
                model.addAttribute("status", "Coming-soon");
            }
        }
        model.addAttribute("userDTO", new UserDTO());
        return "movie_showing";
    }


}
