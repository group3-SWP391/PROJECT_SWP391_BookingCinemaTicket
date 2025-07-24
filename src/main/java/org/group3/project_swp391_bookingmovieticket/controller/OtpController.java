package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.service.impl.UserService;
import org.group3.project_swp391_bookingmovieticket.service.impl.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/initiate-update-profile")
    public String initiateUpdateProfile(@ModelAttribute User user,
                                        HttpSession session, Model model) {
        try {
            User currentUser = getSessionUser(session);

            if (userService.isEmailExists(user.getEmail()) && !user.getEmail().equals(currentUser.getEmail())) {
                model.addAttribute("error", "Email đã tồn tại trong hệ thống.");
                model.addAttribute("user", currentUser);
                return "myAccount";
            }

            if (userService.isPhoneExists(user.getPhone()) && !user.getPhone().equals(currentUser.getPhone())) {
                model.addAttribute("error", "Số điện thoại đã tồn tại trong hệ thống.");
                model.addAttribute("user", currentUser);
                return "myAccount";
            }

            // CB tạo usermoi
            User pendingUser = new User(currentUser);
            pendingUser.setFullname(user.getFullname());
            pendingUser.setEmail(user.getEmail());
            pendingUser.setPhone(user.getPhone());

            String otp = generateOtp();
            session.setAttribute("otp", otp);
            session.setAttribute("pendingUser", pendingUser);

            userService.initiateUpdateProfile(currentUser);

            model.addAttribute("userId", currentUser.getId());
            model.addAttribute("action", "UPDATE_PROFILE_EMAIL");
            return "otp-verification";
        } catch (Exception e) {
            logger.error("Error initiating profile update", e);
            model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            model.addAttribute("user", getSessionUserOrNew(session));
            return "myAccount";
        }
    }

    @PostMapping("/initiate-change-password")
    public String initiateChangePassword(@RequestParam("currentPassword") String currentPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         HttpSession session, Model model) {
        try {
            User user = getSessionUser(session);
            if (userService.findByUsernameAndPassword(user.getEmail(), currentPassword).isPresent()) {
                session.setAttribute("newPassword", newPassword);
                userService.initiateChangePassword(user.getId(), user.getEmail());

                model.addAttribute("userId", user.getId());
                model.addAttribute("action", "CHANGE_PASSWORD_EMAIL");
                return "otp-verification";
            } else {
                model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
                model.addAttribute("user", user);
                return "myAccount";
            }
        } catch (Exception e) {
            logger.error("Error initiating password change", e);
            model.addAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            model.addAttribute("user", getSessionUserOrNew(session));
            return "myAccount";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otpCode") String otpCode,
                            @RequestParam("userId") Integer userId,
                            @RequestParam("action") String action,
                            HttpSession session, Model model) {
        try {
            logger.info("Verifying OTP: {} for userId: {}, action: {}", otpCode, userId, action);
            boolean isValid = false;

            if (action.equals("UPDATE_PROFILE_EMAIL")) {
                User user = (User) session.getAttribute("pendingUser");
                String realOtp = (String) session.getAttribute("otp");
                isValid = otpCode.equals(realOtp);
                if (isValid && user != null) {
                    userService.save(user);
                    session.setAttribute("userLogin", user);
                    session.removeAttribute("pendingUser");
                    session.removeAttribute("otp");
                    model.addAttribute("success", "Cập nhật thông tin thành công.");
                }
            } else if (action.equals("CHANGE_PASSWORD_EMAIL")) {
                String newPassword = (String) session.getAttribute("newPassword");
                isValid = newPassword != null && userService.verifyChangePassword(userId, otpCode, newPassword);
                if (isValid) {
                    session.removeAttribute("newPassword");
                    model.addAttribute("success", "Đổi mật khẩu thành công.");
                }
            }

            if (!isValid) {
                model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn.");
            }

            model.addAttribute("user", getSessionUserOrNew(session));
            return "myAccount";

        } catch (Exception e) {
            logger.error("Error verifying OTP", e);
            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình xác minh.");
            model.addAttribute("user", getSessionUserOrNew(session));
            return "myAccount";
        }
    }

    private User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) throw new IllegalStateException("Session user not found");
        return user;
    }

    private User getSessionUserOrNew(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        return (user != null) ? user : new User();
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    @ModelAttribute("vouchers")
    public List<Voucher> loadVouchers(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user != null) {
            return voucherService.getValidVouchers(user.getId());
        }
        return List.of();
    }
}
