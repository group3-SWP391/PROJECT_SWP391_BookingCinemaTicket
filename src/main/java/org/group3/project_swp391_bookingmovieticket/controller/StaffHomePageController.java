package org.group3.project_swp391_bookingmovieticket.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.IBranchService;
import org.group3.project_swp391_bookingmovieticket.services.IMovieService;
import org.group3.project_swp391_bookingmovieticket.services.IScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class StaffHomePageController {
    @Autowired
    private IScheduleService iScheduleService;
    @Autowired
    private IMovieService iMovieService;
    @Autowired
    private IBranchService iBranchService;
    @Autowired
    private ITicketService iTicketService;
    @GetMapping("/page")
    public String showHomPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session == null){
            return "redirect:/";
        }else{
            User user = (User)session.getAttribute("userLogin");
            if(user != null){
                model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
                return "employee/dashboard";
            }else{
                return "redirect:/";
            }
        }
    }
    @GetMapping("/searchmovie")
    public String showConditionMovie(@RequestParam String movie, @RequestParam("branch") Integer branchId, Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userLogin");
        if(user == null){
            return "redirect:/";
        }
        Optional<Movie> optionalMovie = iMovieService.getMovieByName(movie);
        if (optionalMovie.isPresent()) {
            //Sau khi có được tên phim rồi thì cần xác định với tên phim như thế thì có những lịch trình nào.
            Optional<Branch> optionalBranch = iBranchService.findById(branchId);
            //Có với branch đó.
            if (optionalBranch.isPresent()) {
                Integer idBranch = optionalBranch.get().getId();
                String  idMovie = optionalMovie.get().getName();
                //Lấy lịch trình của ngày hiện tại cùng với 3 ngày sau đó
                LocalDate today = LocalDate.now();
                LocalDate threeDay = today.plusDays(3);
                List<Schedule> scheduleList = iScheduleService.findSchedulesByBranchMovieAndDateRange(idBranch, idMovie, today, threeDay);
                HashMap<Schedule, Integer> ticketIntegerHashMap = iScheduleService.getTicketCountBySchedule(scheduleList);
                model.addAttribute("moviesearch", movie);
                model.addAttribute("movieTicketCount", ticketIntegerHashMap.entrySet());
                model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
            //Có phim đó nhưng ở branch khác.
            }

        }else{
            model.addAttribute("message", "Rất tiếc, không có suất chiếu nào cho phim "+movie+" bạn đang tìm kiếm");
            model.addAttribute("branch", iBranchService.findById(user.getBranch().getId()).get());
            model.addAttribute("moviesearch", movie);
            return "employee/dashboard";

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
