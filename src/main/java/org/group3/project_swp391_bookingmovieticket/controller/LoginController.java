package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;


    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = (User) session.getAttribute("userLogin");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập.");
            return "redirect:/login";
        }

        user.setId(currentUser.getId());

        if (userService.isEmailOrPhoneExists(user)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin. Thay đổi có thể sai hoặc thông tin đã tồn tại trên hệ thống.");
            return "redirect:/my-account";
        }

        try {
            userService.update(user);
            session.setAttribute("userLogin", user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }

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

}
