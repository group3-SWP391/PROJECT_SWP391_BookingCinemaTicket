package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IRoleService;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class CustomerController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;
    @GetMapping("/add")
    public String addCustomerPage(Model model){
        Optional<Role> role =  iRoleService.findByName("customer");
        Role role1 = role.get();
        User user = new User();
        user.setRole(role1);
        model.addAttribute("user", user);
        return "admin/add-user";
    }
    @PostMapping("/add")
    public String saveCustomer(@ModelAttribute("user") User user){
        iUserService.save(user);
        return "redirect:/admin/user";
    }

    @GetMapping ("/delete/{id}")
    public String deleteCustomer(@PathVariable int id, Model model){
        try {
            iUserService.delete(id);
            return "redirect:/admin/user";

        }catch (IllegalArgumentException iae){
            model.addAttribute("error", iae.getMessage());


        }
        return "admin/error";

    }
    @GetMapping("/showformupdate/{id}")
    public String showFormForUpdate(@PathVariable int id, Model model){
          try {
              Optional<User> user = iUserService.getUserByID(id);
              model.addAttribute("user", user.get());
              return "admin/update-customer";

          }catch (IllegalArgumentException iae){
              model.addAttribute("error", iae.getMessage());


          }
          return "admin/error";
    }
    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("user") User user){
        iUserService.save(user);
        return "redirect:/admin/user";

    }


}
