package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.IRoleService;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(role.get());

        model.addAttribute("employee", userDTO);
        return "admin/add-employee";

    }
    @PostMapping("employee/add")
    public String saveEmployee(@ModelAttribute("employee") @Valid UserDTO userDTO, BindingResult result){
        if(result.hasErrors()){

            return "admin/add-employee";

        }
        //chuyển từ DTO - > entity.

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFullname(userDTO.getFullname());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());



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
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.get().getId());
            userDTO.setFullname(user.get().getFullname());
            userDTO.setUsername(user.get().getUsername());
            userDTO.setPassword(user.get().getPassword());
            userDTO.setEmail(user.get().getEmail());
            userDTO.setPhone(user.get().getPhone());
            userDTO.setRole(user.get().getRole());

            model.addAttribute("employee", userDTO);
            return "admin/update-employee";

        }catch (IllegalArgumentException iae){
            model.addAttribute("error", iae.getMessage());


        }
        return "admin/error";
    }
    @PostMapping("/employee/update")
    public String updateCustomer(@ModelAttribute("employee")@Valid UserDTO userDTO, BindingResult result){
        if(result.hasErrors()){
            return "admin/update-employee";
        }
        //chuyển từ DTO - > entity.
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setFullname(userDTO.getFullname());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());

        iUserService.save(user);
        return "redirect:/admin/employee";

    }
    @GetMapping("employee/search")
    public String searchUsers(@RequestParam String keyword, Model model) {
        try{
            Optional<List<User>> userList = iUserService.findByUserNameIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
            if(userList.isPresent()){
                List<User> users = new ArrayList<>();
                for(User user: userList.get()){
                    if(user.getRole().getName().equalsIgnoreCase("Employee")){
                        users.add(user);
                    }
                }
                model.addAttribute("list", users);
            }else{
                model.addAttribute("message", "No user found with this keyword"+keyword);
            }
            return "admin/list-page-employee";


        }catch (IllegalArgumentException iae){
            model.addAttribute("error", iae.getMessage());
        }
        return "admin/error1";

    }





}
