package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPage {
    @Autowired
    private IUserService iUserService;

    @GetMapping
    public String adminHomePage(){
        return "admin/home-page-admin";

    }
    @GetMapping("/user")
    public String getAllUser(Model model){
        List<User> listUser = new ArrayList<>();
        List<User> list =  iUserService.findAll();

        for (User user: list){
            if("Customer".equalsIgnoreCase(user.getRole().getName())){
                listUser.add(user);
            }
        }
        model.addAttribute("list", listUser);
        return "admin/list-page-customer";

    }
    @GetMapping("/employee")
    public String getAllEmployee(Model model){
        List<User> listUser = new ArrayList<>();
        for (User user: iUserService.findAll() ){
            if(user.getRole().getName().equalsIgnoreCase("STAFF")){
                listUser.add(user);
            }
        }
        model.addAttribute("list", listUser);
        return "admin/list-page-employee";

    }







}
