package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.BranchService;
import org.group3.project_swp391_bookingmovieticket.services.impl.EventService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.MOVIE_HIGH_VIEW;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_DTO;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BranchService branchService;

    @GetMapping
    public String displayHomePage(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("categoryAll", movieService.getMovieCategories());
        request.getSession().setAttribute("allLocationBranch", branchService.findAllLocationBranch());
        request.getSession().setAttribute("event", eventService.findEventValid());
        model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
        model.addAttribute(USER_DTO, new UserDTO());
        return "home";
    }

    @GetMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute(USER_DTO, new UserDTO());
        return "contact";
    }

}
