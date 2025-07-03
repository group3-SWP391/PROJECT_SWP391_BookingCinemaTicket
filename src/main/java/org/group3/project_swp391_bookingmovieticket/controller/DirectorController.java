package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.DirectorService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;

@Controller
public class DirectorController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private DirectorService directorService;

    @GetMapping("/director-detail")
    public String directorDetail(@RequestParam Integer directorId,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "3") int size,
                                 Model model) {
        if (directorId == null) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("movieByDirector", movieService.findMovieByDirector(directorId, pageable));
        model.addAttribute("director", directorService.findById(directorId));
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        return "director_detail";
    }
}
