package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.BranchService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;


@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/movieId")
    public String scheduleByMovieId(@RequestParam(value = "movieId", required = false) Integer movieId,
                                    Model model) {
        if (movieId != null || movieId < 0) {
            model.addAttribute("movieDetail", movieService.findMovieById(movieId));
            model.addAttribute("branchByMovie", branchService.getBranchByMovie(movieId));
            model.addAttribute("allStartDateByMovie", scheduleService.getAllStartDateScheduleByMovieId(movieId));
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        } else {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        return "movie_booking";
    }

    @GetMapping("/branch-by-start-date")
    public String findBranchByStartDate(@RequestParam("movieId") int movieId,
                                        @RequestParam("startDate") String startDate,
                                        Model model) {
        model.addAttribute("movieDetail", movieService.findMovieById(movieId));
        model.addAttribute("branchByMovie", branchService.getBranchByStartDate(movieId, startDate));
        model.addAttribute("allStartDateByMovie", scheduleService.getAllStartDateScheduleByMovieId(movieId));
        model.addAttribute("selectedDate", startDate);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "movie_booking";
    }

}
