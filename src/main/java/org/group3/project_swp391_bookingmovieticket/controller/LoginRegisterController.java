package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.TicketEmailService;
import org.group3.project_swp391_bookingmovieticket.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_LOGIN_DTO;
import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_REGISTER_DTO;

@Controller
@RequestMapping
public class LoginRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketEmailService ticketEmailService;

    @GetMapping("/login")
    public String userLoginGet(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "home";
    }

    @PostMapping("/login")
    public String userLoginPost(@Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {
        String redirectUrl = request.getParameter("redirectUrl");
        if (bindingResult.hasErrors()) {
            // Debugging: In ra các lỗi trong BindingResult
            System.out.println("Có lỗi trong form: " + bindingResult.getAllErrors());

            // Đảm bảo form sẽ được hiển thị lại với thông báo lỗi
            model.addAttribute("showLoginModal", true);
            model.addAttribute("redirectUrl", redirectUrl); // Để Thymeleaf giữ lại
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            model.addAttribute(USER_LOGIN_DTO, userLoginDTO);
            return "home";  // Trả về trang home mà không redirect
        }
        User user = new User();
        user.setEmail(userLoginDTO.getEmailLogin());
        user.setPassword(userLoginDTO.getPasswordLogin());
        Optional<User> userExist = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (userExist.isPresent()) {
            // lưu user lên session để phân quyền và lấy thông tin
            request.getSession().setAttribute("userLogin", userExist.get());
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/home";
        } else {
            model.addAttribute("errorLogin", "Invalid username or password");
            model.addAttribute("showLoginModal", true);
            model.addAttribute("userLoginDTO", userLoginDTO);
            model.addAttribute("redirectUrl", redirectUrl);
            System.out.println("lỗi sai");
            return "redirect:/home"
                    + (redirectUrl != null ? "?redirectUrl=" + URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8) : "?")
                    + "&showLoginModal=true";
        }
    }

    @GetMapping("/log-out")
    public String signOut(HttpSession session) {
        session.removeAttribute("userLogin");
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String userRegisterPost(@ModelAttribute("userRegisterDTO") @Valid UserRegisterDTO userRegisterDTO,
                                   BindingResult result,
                                   Model model,
                                   HttpSession session) {
        if (userService.existsByEmail(userRegisterDTO.getEmail())) {
            model.addAttribute("errorMessageRegister", "Email already exists.");
            model.addAttribute("showRegisterModal", true);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        if (userService.existsByUsername(userRegisterDTO.getUserName())) {
            model.addAttribute("errorMessageRegister", "Username already exists.");
            model.addAttribute("showRegisterModal", true);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home";
        }
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("showRegisterModal", true);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, userRegisterDTO);
            return "home";
        }

        String verificationCode = ticketEmailService.generateVerificationCode();
        ticketEmailService.sendVerificationEmail(userRegisterDTO.getEmail(), verificationCode);

        session.setAttribute("verificationCodeRegister", verificationCode);
        session.setAttribute("verificationCodeTimestamp", System.currentTimeMillis());
        session.setAttribute("userRegisterDTO", userRegisterDTO);

        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        return "confirmation_code_register";
    }

    @PostMapping("/verify-register")
    public String verify(@RequestParam String code,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // Get stored verification code and timestamp
        String correctCode = (String) session.getAttribute("verificationCodeRegister");
        Long timestamp = (Long) session.getAttribute("verificationCodeTimestamp");
        UserRegisterDTO userRegisterDTO = (UserRegisterDTO) session.getAttribute("userRegisterDTO");

        if (correctCode == null || timestamp == null) {
            model.addAttribute("errorWrongCode", "Verification code has expired. Please request a new one.");
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_register";
        }

        // Check if the code has expired (2 minutes = 2 * 60 * 1000 milliseconds)
        long expirationTime = 2 * 60 * 1000;
        if (System.currentTimeMillis() - timestamp > expirationTime) {
            model.addAttribute("errorWrongCode", "Verification code has expired. Please request a new one.");
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_register";
        }

        // If code matches, redirect to success page
        if (code.equals(correctCode)) {
            Role customerRole = new Role();
            customerRole.setId(2);
            User user =
                    new User(userRegisterDTO.getUserName(), userRegisterDTO.getPassword(),
                    userRegisterDTO.getFullName(), userRegisterDTO.getEmail(), userRegisterDTO.getPhone());
            user.setRole(customerRole);
            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessageRegister", "Registration successful! You can now proceed to your account.");
            redirectAttributes.addFlashAttribute("showLoginModal", true);
            return "redirect:/home";
        } else {
            model.addAttribute("errorWrongCode", "Invalid verification code. Please try again.");
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_register";
        }
    }

    @PostMapping("/resend-verification-code")
    public String resendVerificationCode(HttpSession session, Model model) {
        // Lấy đối tượng userRegisterDTO từ session
        UserRegisterDTO userRegisterDTO = (UserRegisterDTO) session.getAttribute("userRegister");

        // Kiểm tra nếu userRegisterDTO là null
        if (userRegisterDTO == null) {
            model.addAttribute("errorMessage", "User information not found. Please register first.");
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "home"; // Chuyển hướng về trang đăng ký hoặc trang khác tùy vào yêu cầu
        }

        // Lấy thời gian mã xác nhận đã được gửi từ session (tính theo mili giây)
        Long lastVerificationTime = (Long) session.getAttribute("verificationCodeTimestamp");

        // Tính thời gian chênh lệch giữa thời gian hiện tại và thời gian mã gửi
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastVerificationTime;

        // Nếu chưa đủ 2 phút (2 phút = 2 * 60 * 1000 mili giây), không cho gửi lại mã
        if (timeDifference < 2 * 60 * 1000) {
            long remainingTime = (2 * 60 * 1000 - timeDifference) / 1000;  // Thời gian còn lại trong giây
            model.addAttribute("errorMessage", "You can only resend the verification code after " + remainingTime + " seconds.");
            model.addAttribute("userRegisterDTO", userRegisterDTO);
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_register";
        }

        // Nếu đã đủ 5 phút, gửi lại mã xác nhận
        String newVerificationCode = ticketEmailService.generateVerificationCode();

        // Gửi mã xác nhận qua email
        ticketEmailService.sendVerificationEmail(userRegisterDTO.getEmail(), newVerificationCode);

        // Lưu mã xác nhận mới và thời gian gửi vào session
        session.setAttribute("verificationCodeRegister", newVerificationCode);
        session.setAttribute("verificationCodeTimestamp", currentTime);

        // Thêm thông báo thành công
        model.addAttribute("successMessage", "A new verification code has been sent to your email.");
        model.addAttribute("userRegisterDTO", userRegisterDTO);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        // Quay lại trang xác nhận
        return "confirmation_code_register";
    }
}
