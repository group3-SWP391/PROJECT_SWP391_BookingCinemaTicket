package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.services.impl.EventService;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.MOVIE_HIGH_VIEW;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_DTO;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public String event(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event";
    }

    @GetMapping("/event-detail")
    public String eventDetail(Model model,
                                   @RequestParam(value = "eventId", required = false, defaultValue = "0") Integer eventId) {
        if (eventId == null || eventId < 0) {
            model.addAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
            model.addAttribute(USER_DTO, new UserDTO());
            return "home";
        } else {
            model.addAttribute("eventDetail", eventService.findEventById(eventId));
            model.addAttribute(USER_DTO, new UserDTO());
        }
        return "event_detail";
    }
}
