package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IRoleService;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;

    @GetMapping("/add/employee")
    public String addEmployeePage(Model model){
        Optional<Role> role =  iRoleService.findByName("employee");
        Role role1 = role.get();
        User user = new User();
        user.setRole(role1);
        model.addAttribute("employee", user);
        return "admin/add-employee";

    }
    @PostMapping("employee/add")
    public String saveEmployee(@ModelAttribute("employee") User user){
        iUserService.save(user);
        return "redirect:/admin/employee";
    }
    @GetMapping("/employee/delete/{id}")
    public String delete(@PathVariable int id, Model model){
        try {
            iUserService.delete(id);
            return "redirect:/admin/employee";

        }catch (IllegalArgumentException iae){
            model.addAttribute("error", iae.getMessage());


        }
        return "admin/error1";
    }
    @GetMapping("/employee/showfromupdate/{id}")
    public String showFormForUpdate(@PathVariable int id, Model model){
        try {
            Optional<User> user = iUserService.getUserByID(id);
            model.addAttribute("employee", user.get());
            return "admin/update-employee";

        }catch (IllegalArgumentException iae){
            model.addAttribute("error", iae.getMessage());


        }
        return "admin/error";
    }
    @PostMapping("/employee/update")
    public String updateCustomer(@ModelAttribute("employee") User user){
        iUserService.save(user);
        return "redirect:/admin/employee";

    }




}
