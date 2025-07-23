package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IBillService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class RevenueController {
    @Autowired
    private IBillService billService;
    @Autowired
    private ITicketService ticketService;
    @GetMapping("/revenue")
    public String showRevenuePage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userLogin");
        if(user == null){
            return "redirect:/";
        }else{

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay(); // 2025-07-23T00:00:00
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 2025-07-23T23:59:59.999999999
            List<Bill> billList = billService.findAllByUserIdAndCreatedTimeBetween(user.getId(), startOfDay, endOfDay);
            if(billList.isEmpty()){
                Double totalPrice = 0.0;
                model.addAttribute("totalprice", totalPrice);
                model.addAttribute("message", "Chưa có hóa đơn nào được ghi nhận cho nhân viên trong ngày");
                return "employee/revenueinday";

            }else{
                Map<Bill, Integer> billIntegerMap = billService.countBill(billList);
                Double totalPrice = 0.0;
                for (Bill bill :billIntegerMap.keySet()){
                    totalPrice+=bill.getPrice();
                }
                model.addAttribute("billlist", billIntegerMap.entrySet());
                model.addAttribute("totalprice", totalPrice);
                return "employee/revenueinday";

            }

        }
    }
    @PostMapping("/view/bill")
    public String viewDetailBill(@RequestParam("billid") Integer id, Model model){
        List<Ticket> ticketLists = ticketService.getListTicketByBillId(id);
        model.addAttribute("ticketlist", ticketLists);
        return "employee/viewdetailbill";
    }


}
