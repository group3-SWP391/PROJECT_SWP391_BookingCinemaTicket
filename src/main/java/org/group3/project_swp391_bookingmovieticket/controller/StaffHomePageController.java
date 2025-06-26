package org.group3.project_swp391_bookingmovieticket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.group3.project_swp391_bookingmovieticket.services.IMovieService;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class StaffHomePageController {
    @Autowired
    private IMovieService iMovieService;
    @Autowired
    private IBranchService iBranchService;
    @Autowired
    private ITicketService iTicketService;
    @GetMapping("/page")
    public String showHomPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userLogin");
        model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
        return "employee/dashboard";


    }
    @GetMapping("/searchshowtime")
    public String showConditionMovie(@RequestParam String date, @RequestParam String fromHour, @RequestParam String branch, Model model,HttpServletRequest request){
        HashMap<Ticket, Integer> ticketList = iTicketService.getMovieStatusByTicketCount(date, fromHour, branch);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userLogin");
        if(!ticketList.isEmpty()){

            model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
            model.addAttribute("movieTicketCount", ticketList.entrySet());


        }  else{
            model.addAttribute("message", "No schedule available.");
            model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
        }


        return "employee/dashboard";


    }
    @GetMapping("/movie/detail/{id}")
    public String showMovieDetail(@PathVariable("id") int idMovie, Model model){
        Optional<Movie> movie = iMovieService.findById(idMovie);
        if(movie.isEmpty()){
            model.addAttribute("message", "No movie found with the provided ID. Please check again!!!");
        }else{
            model.addAttribute("movie", movie.get());

        }
        return "employee/movieDetail";



    }


}
