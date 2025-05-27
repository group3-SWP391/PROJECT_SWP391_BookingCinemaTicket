package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String userLoginGet(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    @PostMapping("/login")
    public String userLoginPost(@ModelAttribute UserDTO userDTO,
                                  Model model, HttpServletRequest request) {
        User user = new User();
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        Optional<User> userExist = userService.findByPhoneAndPassword(user.getPhone(), user.getPassword());
        if (userExist.isPresent()) {
            System.out.println(userExist.get());
            // lưu user lên session để phân quyền và lấy thông tin
            request.getSession().setAttribute("userLogin", userExist.get());
            return "redirect:/";
        } else {
            System.out.println("Invalid username or password");
            model.addAttribute("errorLogin", "Invalid username or password");
            // truyền tiếp new UserDTO vào form để form blind được new UserDTO tiếp
            model.addAttribute("userDTO", new UserDTO());
            return "home";
        }
    }

    @GetMapping("/log-out")
    public String signOut(HttpSession session) {
        session.removeAttribute("userLogin");
        return "redirect:/";
    }

}
