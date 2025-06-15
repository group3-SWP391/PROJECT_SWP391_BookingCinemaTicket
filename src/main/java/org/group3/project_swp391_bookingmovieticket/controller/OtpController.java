package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/initiate-update-profile")
    public String initiateUpdateProfile(@ModelAttribute User user, HttpSession session, Model model) {
        try {
            session.setAttribute("pendingUser", user);
            userService.initiateUpdateProfile(user);
            model.addAttribute("userId", user.getId());
            model.addAttribute("action", "UPDATE_PROFILE");
            return "otp-verification";
        } catch (Exception e) {
            logger.error("Error initiating update profile for userId: {}", user.getId(), e);
            model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            // Đảm bảo user không null khi render
            User currentUser = (User) session.getAttribute("userLogin");
            model.addAttribute("user", currentUser != null ? currentUser : new User());
            return "myAccount";
        }
    }

    @PostMapping("/initiate-change-password")
    public String initiateChangePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("userLogin");
            if (user == null) {
                logger.warn("userLogin is null in session");
                model.addAttribute("error", "Phiên đã hết hạn. Vui lòng đăng nhập lại.");
                // Đảm bảo user không null khi render
                model.addAttribute("user", new User());
                return "myAccount";
            }
            if (userService.findByUsernameAndPassword(user.getEmail(), currentPassword).isPresent()) {
                session.setAttribute("newPassword", newPassword);
                userService.initiateChangePassword(user.getId(), user.getEmail());
                model.addAttribute("userId", user.getId());
                model.addAttribute("action", "CHANGE_PASSWORD");
                return "otp-verification";
            } else {
                logger.info("Current password is incorrect for userId: {}", user.getId());
                model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
                // Đảm bảo user không null khi render
                model.addAttribute("user", user); // Sử dụng user từ session
                return "myAccount";
            }
        } catch (Exception e) {
            User sessionUser = (User) session.getAttribute("userLogin");
            String userId = (sessionUser != null) ? String.valueOf(sessionUser.getId()) : "unknown";
            logger.error("Error initiating change password for userId: {}", userId, e);
            model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            // Đảm bảo user không null khi render
            model.addAttribute("user", sessionUser != null ? sessionUser : new User());
            return "myAccount";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam("otpCode") String otpCode,
            @RequestParam("userId") Integer userId,
            @RequestParam("action") String action,
            HttpSession session, Model model) {
        try {
            logger.info("Verifying OTP: {} for userId: {}, action: {}", otpCode, userId, action);
            if ("UPDATE_PROFILE".equals(action)) {
                User user = (User) session.getAttribute("pendingUser");
                if (user == null) {
                    logger.warn("pendingUser is null for userId: {}", userId);
                    model.addAttribute("error", "Dữ liệu phiên không hợp lệ. Vui lòng thử lại.");
                } else if (userService.verifyUpdateProfile(userId, otpCode, user)) {
                    session.setAttribute("userLogin", user);
                    model.addAttribute("success", "Hồ sơ được cập nhật thành công.");
                } else {
                    logger.info("OTP verification failed for UPDATE_PROFILE");
                    model.addAttribute("error", "OTP không hợp lệ hoặc đã hết hạn, vui lòng thử lại.");
                }
            } else if ("CHANGE_PASSWORD".equals(action)) {
                String newPassword = (String) session.getAttribute("newPassword");
                if (newPassword == null) {
                    logger.warn("newPassword is null for userId: {}", userId);
                    model.addAttribute("error", "Dữ liệu phiên không hợp lệ. Vui lòng thử lại.");
                } else if (userService.verifyChangePassword(userId, otpCode, newPassword)) {
                    model.addAttribute("success", "Đã thay đổi mật khẩu thành công.");
                } else {
                    logger.info("OTP verification failed for CHANGE_PASSWORD");
                    model.addAttribute("error", "OTP không hợp lệ hoặc đã hết hạn, vui lòng thử lại.");
                }
            }
            User currentUser = (User) session.getAttribute("userLogin");
            model.addAttribute("user", currentUser != null ? currentUser : new User());
            return "myAccount";
        } catch (Exception e) {
            logger.error("Error verifying OTP for userId: {}, action: {}", userId, action, e);
            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình xác minh. Vui lòng thử lại.");
            User currentUser = (User) session.getAttribute("userLogin");
            model.addAttribute("user", currentUser != null ? currentUser : new User());
            return "myAccount";
        }
    }
}