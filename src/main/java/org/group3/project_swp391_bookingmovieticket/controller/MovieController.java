package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
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

    // chưa fix cần sang trang riêng
//    @GetMapping("/search")
//    public String displayAllMovies(@RequestParam(value = "movieNameSearch", required = false) String movieNameSearch,
//                                   Model model) {
//        if (movieNameSearch == null || movieNameSearch.trim().isEmpty()) {
//            model.addAttribute("movieNowShowing", movieService.findMovieNowShowing());
//        } else {
//            model.addAttribute("movieAll", movieService.findByMovieName(movieNameSearch));
//        }
//        model.addAttribute("categoryAll", movieService.getMovieCategories());
//        model.addAttribute("movieByCategory", movieService.findAll());
//        model.addAttribute("userDTO", new UserDTO());
//        return "home";
//    }

    @GetMapping(value = "/category/return-json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<MovieDTO> getMoviesByCategory(@RequestParam(value = "categoryName", required = false)
                                              String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty() || categoryName.equalsIgnoreCase("movieAll")) {
            return movieService.findAll();
        } else {
            return movieService.getMovieByCategory(categoryName);
        }
    }

    @GetMapping(value = "/detail")
    public String displayMovieDetail(@RequestParam(value = "movieId", required = false) Integer movieId,
                                     Model model) {
        // cần testing lại xem có null đc kh, back về home đc kh
        if (movieId == null) {
            model.addAttribute("movieHighView", movieService.findMovieByViewDesc());
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        }
        // cần load 1 list movie cùng thể loại với movie đang ở detail
        model.addAttribute("movieAll", movieService.findAll());

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
                model.addAttribute("userDTO", new UserDTO());
                model.addAttribute("status", "Now-showing");
            } else if (status.equalsIgnoreCase("coming-soon")) {
                model.addAttribute("movieShowing", movieService.findMovieComingSoon());
                model.addAttribute("userDTO", new UserDTO());
                model.addAttribute("status", "Coming-soon");
            }
        }
        return "movie_showing";
    }


}
