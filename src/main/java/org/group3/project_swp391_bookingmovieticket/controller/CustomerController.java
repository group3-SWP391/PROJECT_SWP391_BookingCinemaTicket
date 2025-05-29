package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Controller
public class CustomerController {
    @Autowired
    private IUserService iUserService;
    @GetMapping("/user/add")
    public String addCustomerPage(Model model){
        Role role = new Role();
        role.setId(3);
        role.setName("customer");
        User user = new User();
        user.setRole(role);
        model.addAttribute("user", user);
        return "admin/add-user";
    }
    @PostMapping("/user/add")
    public String saveCustomer(@ModelAttribute("user") User user){
        iUserService.save(user);
        return "redirect:/admin/user";
    }

//    @DeleteMapping("/user/delete/{id}")
//    public String deleteCustomer(@PathVariable int id){
//        iUserService.delete(id);
//        return "redirect:/admin/employee";
//    }

}
