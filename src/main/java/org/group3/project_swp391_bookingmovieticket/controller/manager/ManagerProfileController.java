package org.group3.project_swp391_bookingmovieticket.controller.manager;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.service.impl.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) return "redirect:/login";

        User user = userRepository.findByPhone(userLogin.getPhone()).get();
        if (user == null) return "redirect:/login";

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        model.addAttribute("manager", userDTO);
        model.addAttribute("content", "manager/profile");

        return "manager/layout";
    }

//    @PostMapping("/profile/update")
//    public String updateProfile(@ModelAttribute("manager") UserDTO userDTO,
//                                HttpSession session,
//                                RedirectAttributes redirectAttributes) {
//        try {
//            User userLogin = (User) session.getAttribute("userLogin");
//            if (userLogin != null && userDTO.getId() == null) {
//                userDTO.setId(userLogin.getId());
//            }
//            userService.updateProfile(userDTO);
//            User updatedUser = userService.findByPhone(userDTO.getPhone());
//            session.setAttribute("userLogin", updatedUser);
//            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
//        }
//        return "redirect:/manager/profile";
//    }
//
//    @PostMapping("/profile/change-password")
//    public String changePassword(@RequestParam String oldPassword,
//                                 @RequestParam String newPassword,
//                                 HttpSession session,
//                                 RedirectAttributes redirectAttributes) {
//        User userLogin = (User) session.getAttribute("userLogin");
//        if (userLogin == null) return "redirect:/login";
//
//        String phone = userLogin.getPhone();
//        boolean success = userService.changePassword(phone, oldPassword, newPassword);
//        if (success) {
//            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
//        } else {
//            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
//        }
//        return "redirect:/manager/profile";
//    }
}