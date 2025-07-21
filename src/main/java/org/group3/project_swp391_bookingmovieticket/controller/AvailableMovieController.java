package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class AvailableMovieController {
    @Autowired
    private IScheduleService iScheduleService;

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
                List<Schedule> scheduleList = iScheduleService.findSchedulesByBranchAndDay(user.getBranch().getId(), localDateDay);
                HashMap<Schedule, Integer> scheduleIntegerHashMap = iScheduleService.getTicketCountBySchedule(scheduleList);
                model.addAttribute("availablemovie", scheduleIntegerHashMap.entrySet());
                return "employee/availablemovie";
            }else{
                return "redirect:/";
            }
        }




    }
}
