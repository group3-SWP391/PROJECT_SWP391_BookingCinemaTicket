package org.group3.project_swp391_bookingmovieticket.controller.staff;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.service.IBranchService;
import org.group3.project_swp391_bookingmovieticket.service.IMovieService;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class StaffHomePageController {
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IMovieService movieService;
    @Autowired
    private IBranchService branchService;
    @Autowired
    private ITicketService ticketService;
    @GetMapping("/page")
    public String showHomPage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session == null){
            return "redirect:/";
        }else{
            User user = (User)session.getAttribute("userLogin");
            if(user != null){
                model.addAttribute("branch", branchService.findById(user.getBranch().getId()).get());
                return "employee/dashboard";
            }else{
                return "redirect:/";
            }
        }
    }
    @PostMapping("/searchmovie")
    public String showConditionMovie(@RequestParam String movie, @RequestParam("branch") Integer branchId, Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userLogin");
        if(user == null){
            return "redirect:/";
        }
        Optional<Movie> optionalMovie = movieService.getMovieByName(movie);
        if (optionalMovie.isPresent()) {
            //Sau khi có được tên phim rồi thì cần xác định với tên phim như thế thì có những lịch trình nào.
            Optional<Branch> optionalBranch = branchService.findById(branchId);
            //Có với branch đó.
            if (optionalBranch.isPresent()) {
                Integer idBranch = optionalBranch.get().getId();
                String  idMovie = optionalMovie.get().getName();
                //Lấy lịch trình của ngày hiện tại cùng với 3 ngày sau đó
                LocalDate today = LocalDate.now();
                LocalDate threeDay = today.plusDays(10);
                List<Schedule> scheduleList = scheduleService.findSchedulesByBranchMovieAndDateRange(idBranch, idMovie, today, threeDay);
                HashMap<Schedule, Integer> ticketIntegerHashMap = scheduleService.getTicketCountBySchedule(scheduleList);
                model.addAttribute("moviesearch", movie);
                model.addAttribute("movieTicketCount", ticketIntegerHashMap.entrySet());
                model.addAttribute("branch", branchService.findById(user.getBranch().getId()).get());
            //Có phim đó nhưng ở branch khác.
            }

        }else{
            model.addAttribute("message", "Rất tiếc, không có suất chiếu nào cho phim "+movie+" bạn đang tìm kiếm");
            model.addAttribute("branch", branchService.findById(user.getBranch().getId()).get());
            model.addAttribute("moviesearch", movie);
            return "employee/dashboard";

        }
        return "employee/dashboard";
    }
    @GetMapping("/movie/detail/{id}")
    public String showMovieDetail(@PathVariable("id") int idMovie, Model model){
        Optional<Movie> movie = movieService.findById(idMovie);
        if(movie.isEmpty()){
            model.addAttribute("messageerror", "Không tìm thấy bộ phim với ID đã cung cấp. Vui lòng kiểm tra lại!!!");
            return "employee/error";
        }else{
            model.addAttribute("movie", movie.get());
            return "employee/movieDetail";

        }
    }




}
