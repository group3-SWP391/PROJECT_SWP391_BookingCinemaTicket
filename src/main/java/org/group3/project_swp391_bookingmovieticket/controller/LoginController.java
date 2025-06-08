package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-account")
    public String myAccount(Model model, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            model.addAttribute("error", "Please log in to access your account.");
            return "redirect:/login";
        }
        model.addAttribute("user", user); // Truyền User từ session
        return "myAccount"; // Trả về myAccount.html
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("userLogin");
        if (currentUser == null) {
            model.addAttribute("error", "Please log in to update your profile.");
            return "redirect:/login";
        }
        user.setId(currentUser.getId()); // Gán ID từ session
        userService.update(user); // Cập nhật với password hiện tại
        session.setAttribute("userLogin", user); // Cập nhật session (chỉ full_name, phone, email)
        model.addAttribute("success", "Profile updated successfully!");
        return "redirect:/my-account";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
                                 Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("userLogin");
        if (currentUser == null) {
            model.addAttribute("error", "Please log in to change your password.");
            return "redirect:/login";
        }
        System.out.println("Attempting to change password for userId: " + currentUser.getId() + ", phone: " + currentUser.getPhone());
        if (userService.findByPhoneAndPassword(currentUser.getPhone(), currentPassword).isPresent()) {
            userService.changePassword(currentUser.getId(), newPassword);
            model.addAttribute("success", "Password changed successfully!");
            System.out.println("Password changed successfully for userId: " + currentUser.getId());
        } else {
            model.addAttribute("error", "Current password is incorrect.");
            System.out.println("Current password incorrect for userId: " + currentUser.getId());
        }
        return "redirect:/my-account";
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
            request.getSession().setAttribute("userLogin", userExist.get());
            return "redirect:/";
        } else {
            System.out.println("Invalid username or password");
            model.addAttribute("errorLogin", "Invalid username or password");
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