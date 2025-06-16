package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String userLoginGet(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "home";
    }

    @PostMapping("/login")
    public String userLoginPost(@ModelAttribute("userLoginDTO") @Valid UserLoginDTO userLoginDTO,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("showLoginModal", true);
            model.addAttribute("errorLogin", "Please check your input.");
            return "home";
        }
        User user = new User();
        user.setEmail(userLoginDTO.getEmail());
        user.setPassword(userLoginDTO.getPassword());
        Optional<User> userExist = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userExist.isPresent()) {
            // lưu user lên session để phân quyền và lấy thông tin
            request.getSession().setAttribute("userLogin", userExist.get());
            return "home";
        } else {
            model.addAttribute("errorLogin", "Invalid username or password");
            model.addAttribute("showLoginModal", true);
            model.addAttribute("userLoginDTO", new UserLoginDTO());
            return "home";
        }
    }

    @GetMapping("/log-out")
    public String signOut(HttpSession session) {
        session.removeAttribute("userLogin");
        return "redirect:/";
    }

}
