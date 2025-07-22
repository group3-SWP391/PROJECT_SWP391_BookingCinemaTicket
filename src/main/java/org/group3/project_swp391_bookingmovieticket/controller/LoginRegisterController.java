package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.group3.project_swp391_bookingmovieticket.dto.FacebookAccount;
import org.group3.project_swp391_bookingmovieticket.dto.GoogleAccount;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Role;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.FacebookAccountService;
import org.group3.project_swp391_bookingmovieticket.service.GoogleAccountService;
import org.group3.project_swp391_bookingmovieticket.service.TicketEmailService;
import org.group3.project_swp391_bookingmovieticket.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private GoogleAccountService googleAccountService;

    @Autowired
    private FacebookAccountService facebookAccountService;

    @GetMapping("/login")
    public String userLoginGet(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "home";
    }

    @PostMapping("/login")
    public String userLoginPost(@Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes,
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
        Optional<User> userExist = userService.findByEmailAndPassword(user.getEmail());
        System.out.println(userExist.get().getPassword() + "sql");

        if (userExist.isPresent()) {
            // So sánh mật khẩu người dùng nhập với mật khẩu đã mã hóa trong CSDL
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // lưu user lên session để phân quyền và lấy thông tin
            if (passwordEncoder.matches(userLoginDTO.getPasswordLogin(), userExist.get().getPassword())) {
                // Lưu thông tin người dùng vào session và phân quyền
                request.getSession().setAttribute("userLogin", userExist.get());
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    return "redirect:" + redirectUrl;
                }
                System.out.println("Đã đăng nhập thành công");
                return "redirect:/home";  // Chuyển hướng về trang chủ
            } else {
                redirectAttributes.addFlashAttribute("errorLogin", "Invalid password!");
                redirectAttributes.addFlashAttribute("showLoginModal", true);
                redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
                redirectAttributes.addFlashAttribute("redirectUrl", redirectUrl);
                System.out.println("Lỗi: Mật khẩu không hợp lệ");
                return "redirect:/home"
                        + (redirectUrl != null ? "?redirectUrl=" + URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8) : "?")
                        + "&showLoginModal=true";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorLogin", "Invalid password!");
            redirectAttributes.addFlashAttribute("showLoginModal", true);
            redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
            redirectAttributes.addFlashAttribute("redirectUrl", redirectUrl);
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
            // Mã hóa mật khẩu
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
            Role customerRole = new Role();
            customerRole.setId(2);
            User user =
                    new User(userRegisterDTO.getUserName(), hashedPassword,
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

    @GetMapping("/login-by-gg")
    public String loginByGoogle(@RequestParam("code") String code,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            // 1. Đổi code lấy access token
            String accessToken = googleAccountService.getToken(code);

            // 2. Lấy thông tin người dùng từ Google
            GoogleAccount googleUser = googleAccountService.getUserInfo(accessToken);

            User user;
            if (userService.existsByEmail(googleUser.getEmail())) {
                // Nếu đã tồn tại, lấy user từ DB
                user = userService.findByEmail(googleUser.getEmail()).get(); // nhớ xử lý Optional an toàn
                System.out.println(user + "role");
            } else {
                // Nếu chưa có, tạo user mới
                user = new User();
                user.setEmail(googleUser.getEmail());
                user.setFullname(googleUser.getName());
                user.setUsername(googleUser.getEmail().split("@")[0]);
                user.setPassword(""); // Không cần mật khẩu
                Role role = new Role();
                role.setId(2); // Role "customer"
                user.setRole(role);
                userService.save(user);
            }

            // 5. Đăng nhập và lưu vào session
            session.setAttribute("userLogin", user);
            if (user.getRole().getName().equals("ADMIN") && user != null) {
                return "redirect:/admin";
            }
//            else if (user.getRole().getName().equals("STAFF") && user != null) {
//                return "redirect:/staff";
//            }
            return "redirect:/home";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorLogin", "Google login failed. Please try again.");
            redirectAttributes.addFlashAttribute("showLoginModal", true);
            return "redirect:/home";
        }
    }

    @GetMapping("/login-by-facebook")
    public String loginByFacebook(@RequestParam("code") String code,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            String accessToken = facebookAccountService.getAccessToken(code);
            FacebookAccount fbUser = facebookAccountService.getUserInfo(accessToken);

            User user;
            if (userService.existsByEmail(fbUser.getEmail())) {
                user = userService.findByEmail(fbUser.getEmail()).get();
            } else {
                user = new User();
                user.setEmail(fbUser.getEmail());
                user.setFullname(fbUser.getName());
                user.setUsername(fbUser.getEmail().split("@")[0]);
                user.setPassword("");
                Role role = new Role();
                role.setId(2); // customer
                user.setRole(role);
                userService.save(user);
            }

            session.setAttribute("userLogin", user);
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorLogin", "Facebook login failed.");
            redirectAttributes.addFlashAttribute("showLoginModal", true);
            return "redirect:/home";
        }
    }

    @PostMapping("/verification-code-forget-password")
    public String sendVerificationCode(@RequestParam("email") String email,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session,
                                       Model model) {
        if (!userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorNotFoundEmail", "Email không tồn tại!");
            redirectAttributes.addFlashAttribute("showForgetPassModal", true);
            return "redirect:/home";
        }
        String verificationCode = ticketEmailService.generateVerificationCode();
        ticketEmailService.sendVerificationEmail(email, verificationCode);
        session.setAttribute("verificationCodeForget", verificationCode);
        session.setAttribute("verificationCodeTimestampForget", System.currentTimeMillis());
        session.setAttribute("emailForgetPass", email);
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "confirmation_code_forget_pass";  // Chuyển hướng về trang home hoặc trang thông báo lỗi
    }

    @PostMapping("/verify-forget-pass")
    public String verifyForgetPass(@RequestParam String code,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        // Get stored verification code and timestamp
        String correctCode = (String) session.getAttribute("verificationCodeForget");
        Long timestamp = (Long) session.getAttribute("verificationCodeTimestampForget");
        String email = (String) session.getAttribute("emailForgetPass");

        // Check if the code has expired (2 minutes = 2 * 60 * 1000 milliseconds)
        long expirationTime = 2 * 60 * 1000;
        if (System.currentTimeMillis() - timestamp > expirationTime) {
            model.addAttribute("errorOverTime", "Mã xác nhận đã hết hạn, vui lòng gửi lại!");
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_forget_pass";
        }

        // If code matches, redirect to success page
        if (code.equals(correctCode)) {
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("showChangePass", true);
            return "redirect:/home";
        } else {
            model.addAttribute("errorWrongCodeForgetPass", "Sai mã xác nhận, vui thử lại!");
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_forget_pass";
        }
    }

    @PostMapping("/resend-verification-code-forget-pass")
    public String resendVerificationCodeForgetPass(HttpSession session, Model model) {

        String email = (String) session.getAttribute("emailForgetPass");

        // Lấy thời gian mã xác nhận đã được gửi từ session (tính theo mili giây)
        Long lastVerificationTime = (Long) session.getAttribute("verificationCodeTimestampForget");

        // Tính thời gian chênh lệch giữa thời gian hiện tại và thời gian mã gửi
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastVerificationTime;

        // Nếu chưa đủ 2 phút (2 phút = 2 * 60 * 1000 mili giây), không cho gửi lại mã
        if (timeDifference < 2 * 60 * 1000) {
            long remainingTime = (2 * 60 * 1000 - timeDifference) / 1000;  // Thời gian còn lại trong giây
            model.addAttribute("errorMessage2", "Bạn chỉ có thể gửi lại sau " + remainingTime + " giây!");
            model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
            model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
            return "confirmation_code_forget_pass";
        }

        // Nếu đã đủ 5 phút, gửi lại mã xác nhận
        String newVerificationCode = ticketEmailService.generateVerificationCode();

        // Gửi mã xác nhận qua email
        ticketEmailService.sendVerificationEmail(email, newVerificationCode);

        // Lưu mã xác nhận mới và thời gian gửi vào session
        session.setAttribute("verificationCodeForget", newVerificationCode);
        session.setAttribute("verificationCodeTimestampForget", currentTime);

        // Thêm thông báo thành công
        model.addAttribute("successMessage2", "Mã mới đã được gửi về email của bạn vui lòng kiểm tra!");
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());

        // Quay lại trang xác nhận
        return "confirmation_code_forget_pass";
    }

    @PostMapping("/change-pass-forget")
    public String changePassForget(@RequestParam String email,
                                   @RequestParam String newPassword,
                                   @RequestParam String newPasswordReInput,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        if (!newPassword.equals(newPasswordReInput)) {
            redirectAttributes.addFlashAttribute("errorChangePass", "Vui lòng nhập mật khẩu khớp với nhập lại mật khẩu!");
            redirectAttributes.addFlashAttribute("showChangePass", true);
            return "redirect:/home";
        } else if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorChangePass", "Vui lòng nhập mật khẩu có 6 ký tự trở lên!");
            redirectAttributes.addFlashAttribute("showChangePass", true);
            return "redirect:/home";
        }
        User user = userService.findByEmail(email).get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userService.save(user);
        redirectAttributes.addFlashAttribute("successChangePass", "Đổi mật khẩu thành công vui lòng đăng nhập");
        redirectAttributes.addFlashAttribute("showLoginModal", true);
        return "redirect:/home";
    }
}
