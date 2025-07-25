package org.group3.project_swp391_bookingmovieticket.controller.staff;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IMovieService;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class AvailableMovieController {
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IMovieService movieService;

    @GetMapping("/available-movies")
    public String showAvailableMovies(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session == null){
            return "redirect:/";
        }else{
            User user = (User)session.getAttribute("userLogin");
            if(user != null){

                //Show thông tin xem hôm nay những phim nào còn trống.
                LocalDate localDateDay = LocalDate.now();
                //Lấy ra một list các lịch trình với cái ngày hôm nay.
                List<Schedule> scheduleList = scheduleService.findSchedulesByBranchAndDay(user.getBranch().getId(), localDateDay);
                HashMap<Schedule, Integer> scheduleIntegerHashMap = scheduleService.getTicketCountBySchedule(scheduleList);
                model.addAttribute("availablemovie", scheduleIntegerHashMap.entrySet());
                return "employee/availablemovie";
            }else{
                return "redirect:/";
            }
        }
    }
    @GetMapping("/moviedetail/{id}")
    public String showMovieDetail(@PathVariable("id") Integer movieId, Model model){
        Optional<Movie> movie = movieService.findById(movieId);
        if(movie.isPresent()){
            model.addAttribute("movie", movie.get());
            return "employee/movieDetail";

        }else{
            model.addAttribute("messageerror", "Không tìm thấy bộ phim với ID đã cung cấp. Vui lòng kiểm tra lại!!!");
            return "employee/error";

        }
    }
}
