package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.BranchService;
import org.group3.project_swp391_bookingmovieticket.services.impl.EventService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/home")
    public String displayHomePage(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                  @RequestParam(value = "showLoginModal", required = false) String showLoginModal,
                                  Model model,
                                  HttpServletRequest request) {
        if ("true".equals(showLoginModal)) {
            model.addAttribute("showLoginModal", true);
        }
        if (redirectUrl != null) {
            model.addAttribute("redirectUrl", redirectUrl);
        }
        request.getSession().setAttribute("categoryAll", movieService.getMovieCategories());
        request.getSession().setAttribute("allLocationBranch", branchService.findAllLocationBranch());
        request.getSession().setAttribute("event", eventService.findEventValid());
        request.getSession().setAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "home";
    }

    @GetMapping("/contact")
    public String displayContactPage(Model model) {
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "contact";
    }

}
