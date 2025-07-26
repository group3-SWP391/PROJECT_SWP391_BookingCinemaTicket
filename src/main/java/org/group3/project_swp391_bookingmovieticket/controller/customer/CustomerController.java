package org.group3.project_swp391_bookingmovieticket.controller.customer;


import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.entity.User;

import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.group3.project_swp391_bookingmovieticket.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class CustomerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ITicketService iTicketService;

    @GetMapping("/search")
    public String searchUsers(@RequestParam String keyword, Model model) {
        try {
            Optional<List<User>> userList = iUserService.findByUserNameIgnoreCase(keyword);

            model.addAttribute("keyword", keyword);
            if (userList.isPresent()) {
                List<User> users = new ArrayList<>();
                for (User user : userList.get()) {
                    if (user.getRole().getName().equalsIgnoreCase("Customer")) {
                        users.add(user);
                    }
                }
                model.addAttribute("list", users);
            } else {
                model.addAttribute("message", "No user found with this keyword" + keyword);
            }
            return "admin/list-page-customer";


        } catch (IllegalArgumentException iae) {
            model.addAttribute("error", iae.getMessage());
        }
        return "admin/error";

    }

    @GetMapping("/booking-history/{id}")
    public String getAllBookingHistory(@PathVariable int id, Model model) {
        try {
            List<Ticket> tickets = iTicketService.getListBillByID(id);
            if (tickets.isEmpty()) {
                model.addAttribute("message", "No bookings found.");
            } else {
                model.addAttribute("listbooking", tickets);

            }

            return "admin/listbooking";


        } catch (IllegalArgumentException iae) {
            model.addAttribute("error", iae.getMessage());

        }
        return "admin/error";

    }


}
