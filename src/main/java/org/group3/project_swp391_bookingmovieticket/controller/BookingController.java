package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dto.SeatDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/employee")
public class BookingController {
    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private ISeatService seatService;
    @Autowired
    private ITicketService ticketService;


    @GetMapping("/schedule/selectseat/{id}")
    public String showListSeat(@PathVariable int id, Model model){
        Optional<Schedule> schedule = scheduleService.findById(id);
        if(schedule.isPresent()){

            List<Seat> seatList = seatService.getListSeatById(schedule.get().getRoom().getId());
            HashSet<Integer> bookedSeatIds = ticketService.findBookedSeatIdsBySchedule(id);
            List<SeatDTO> seatDTOList = new ArrayList<>();
            for (Seat seat : seatList) {
                SeatDTO dto = new SeatDTO();
                dto.setId(seat.getId());
                dto.setName(seat.getName());
                dto.setActive(seat.getIsActive());
                dto.setVip(seat.getIsVip());
                dto.setOccupied(bookedSeatIds.contains(seat.getId()));
                dto.setChecked(false);
                seatDTOList.add(dto);
            }
            model.addAttribute("seatList", seatDTOList);
            model.addAttribute("schedule", schedule.get());
            return "employee/seat_selection";

        }else{
            model.addAttribute("messageerror", "Không tồn tại lịch chiếu bạn tìm kiếm");
            return "employee/error";

        }






    }
}
