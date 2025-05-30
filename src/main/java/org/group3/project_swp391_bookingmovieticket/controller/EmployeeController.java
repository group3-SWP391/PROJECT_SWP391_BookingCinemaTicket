package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/add/employee")
    public String addEmployeePage(Model model){
        Role role = new Role();
        role.setId(4);
        role.setName("employee");
        User user = new User();
        user.setRole(role);
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


}
