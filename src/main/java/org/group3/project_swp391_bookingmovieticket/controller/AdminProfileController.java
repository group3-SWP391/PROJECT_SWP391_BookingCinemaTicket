package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) return "redirect:/login";

        User user = userService.findByPhone(userLogin.getPhone());
        if (user == null) return "redirect:/login";

        UserDTO userDTO = userService.convertToDTO(user);
        model.addAttribute("admin", userDTO);
        model.addAttribute("content", "admin/profile");

        return "admin/layout";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("admin") UserDTO userDTO,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(userDTO);
            User updatedUser = userService.findByPhone(userDTO.getPhone());
            session.setAttribute("userLogin", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) return "redirect:/login";

        String phone = userLogin.getPhone();
        boolean success = userService.changePassword(phone, oldPassword, newPassword);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
        }
        return "redirect:/admin/profile";
    }
}
