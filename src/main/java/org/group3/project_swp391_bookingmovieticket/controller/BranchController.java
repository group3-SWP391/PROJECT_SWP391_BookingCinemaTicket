package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.service.impl.BranchService;
import org.group3.project_swp391_bookingmovieticket.service.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;

@Controller
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/branch-by-location")
    public String branchByLocation(Model model,
                                   @RequestParam(value = "locationName", required = false) String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        } else {
            model.addAttribute("locationFind", locationName);
            model.addAttribute("branchByLocation", branchService.findBranchByLocation(locationName));
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        }
        return "branch_location";
    }
}
