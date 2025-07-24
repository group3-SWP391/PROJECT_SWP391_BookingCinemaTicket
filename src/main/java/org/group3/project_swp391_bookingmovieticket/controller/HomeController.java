package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dto.NotificationDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.group3.project_swp391_bookingmovieticket.service.ICommentReactionService;
import org.group3.project_swp391_bookingmovieticket.service.ICommentService;
import org.group3.project_swp391_bookingmovieticket.service.IRatingService;
import org.group3.project_swp391_bookingmovieticket.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.*;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private INotificationRepository INotificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private IAdvertisingContactRequestRepository IAdvertisingContactRequestRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PaymentLinkService orderService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IContactRequestRepository IContactRequestRepository;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ICommentReactionService reactionService;

    @Autowired
    private IVoucherRepository IVoucherRepository;

    @Autowired
    private IPaymentLinkRepository IPaymentLinkRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @GetMapping("/home")
    public String displayHomePage(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                  @RequestParam(value = "showLoginModal", required = false) String showLoginModal,
                                  HttpSession session,
                                  Model model,
                                  HttpServletRequest request) {
        if ("true".equals(showLoginModal)) {
            model.addAttribute("showLoginModal", true);
        }
        if (redirectUrl != null) {
            model.addAttribute("redirectUrl", redirectUrl);
        }

        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            model.addAttribute("notifications", Collections.emptyList());
        } else {
            // Lay tb o database
            List<Notification> notifications = INotificationRepository
                    .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());

            // Chuyển sang DTO để hiển thị gọn trong giao diện
            List<NotificationDTO> notificationDTOs = notifications.stream()
                    .map(n -> new NotificationDTO(n.getMessage(), n.getMovie().getId()))
                    .collect(Collectors.toList());

            model.addAttribute("notifications", notificationDTOs);
            System.out.println("DEBUG: notifications size = " + notificationDTOs.size());
        }


        request.getSession().setAttribute("categoryAll", movieService.getMovieCategories());
        request.getSession().setAttribute("allLocationBranch", branchService.findAllLocationBranch());
        request.getSession().setAttribute("event", eventService.findEventValid());
        request.getSession().setAttribute("rates", ratingService.findAll());
        request.getSession().setAttribute(MOVIE_HIGH_VIEW, movieService.findMovieByViewDesc());
        model.addAttribute(USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    private String generateSecurityCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    @PostMapping("/advertising-contact")
    public String handleAdvertisingContactForm(@RequestParam String fullName,
                                               @RequestParam String phone,
                                               @RequestParam String email,
                                               @RequestParam String companyName,
                                               @RequestParam String companyAddress,
                                               @RequestParam String rentalDate,
                                               @RequestParam(required = false) String notes,
                                               @RequestParam String securityCode,
                                               Model model,
                                               HttpServletRequest request,
                                               HttpSession session) {
        try {
            System.out.println("Received Security Code: " + securityCode);
            String expectedSecurityCode = (String) session.getAttribute("securityCode");
            System.out.println("Expected Security Code: " + expectedSecurityCode);

            if (expectedSecurityCode == null || !expectedSecurityCode.equals(securityCode)) {
                System.out.println("Security code mismatch!");
                model.addAttribute("errorMessage", "Mã bảo mật không chính xác. Vui lòng thử lại!");
                String newSecurityCode = generateSecurityCode(6);
                session.setAttribute("securityCode", newSecurityCode);
                model.addAttribute("securityCode", newSecurityCode);
                model.addAttribute("userDTO", new UserDTO());
                return "advertising-contact";
            }

            System.out.println("Security code matched, proceeding with form processing...");

            AdvertisingContactRequest advertisingContactRequest = new AdvertisingContactRequest();
            advertisingContactRequest.setFullName(fullName);
            advertisingContactRequest.setPhone(phone);
            advertisingContactRequest.setEmail(email);
            advertisingContactRequest.setCompanyName(companyName);
            advertisingContactRequest.setCompanyAddress(companyAddress);
            advertisingContactRequest.setRentalDate(LocalDate.parse(rentalDate));
            advertisingContactRequest.setNotes(notes);
            advertisingContactRequest.setCreatedAt(LocalDateTime.now());

            System.out.println("Saving AdvertisingContactRequest to database...");
            IAdvertisingContactRequestRepository.save(advertisingContactRequest);
            System.out.println("Successfully saved to database. ID: " + advertisingContactRequest.getId());

            String emailSubject = "Xác Nhận Yêu Cầu Liên Hệ Quảng Cáo";
            String emailBody = "Chào " + fullName + ",\n\n" +
                    "Chúng tôi đã nhận được yêu cầu liên hệ quảng cáo từ " + companyName + ". Thông tin chi tiết:\n" +
                    "Địa chỉ: " + companyAddress + "\n" +
                    "Ngày muốn thuê: " + rentalDate + "\n" +
                    "Ghi chú: " + (notes != null ? notes : "Không có") + "\n\n" +
                    "Bộ phận quảng cáo FPT Cinemas của chúng tôi đã ghi nhận yêu cầu của bạn và sẽ liên hệ lại với bạn qua email " + email + " hoặc số điện thoại " + phone + " để báo giá trong thời gian sớm nhất.\n" +
                    "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";

            System.out.println("Sending email to: " + email);
            emailService.sendEmail(email, emailSubject, emailBody);
            System.out.println("Email sent successfully.");

            System.out.println("Adding success message to model...");
            model.addAttribute("successMessage", "Yêu cầu của bạn đã được gửi thành công! Vui lòng kiểm tra email để xem phản hồi.");
            String newSecurityCode = generateSecurityCode(6);
            session.setAttribute("securityCode", newSecurityCode);
            model.addAttribute("securityCode", newSecurityCode);
            model.addAttribute("userDTO", new UserDTO());
            System.out.println("Returning to advertising-contact page with success message.");
            return "advertising-contact";
        } catch (Exception e) {
            System.out.println("Error in handleAdvertisingContactForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra khi gửi yêu cầu: " + e.getMessage());
            String newSecurityCode = generateSecurityCode(6);
            session.setAttribute("securityCode", newSecurityCode);
            model.addAttribute("securityCode", newSecurityCode);
            model.addAttribute("userDTO", new UserDTO());
            return "advertising-contact";
        }
    }

    @GetMapping("/voucher/{id}")
    public String viewVoucherDetail(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "voucher_detail";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        try {
            model.addAttribute("userDTO", new UserDTO());
            return "contact";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while loading the contact page: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/my-account")
    public String myAccount(Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = (User) session.getAttribute("userLogin");
            if (user == null) {
                redirectAttributes.addFlashAttribute("showModalLogin", true);
                return "redirect:/home";
            }

            List<NotificationDTO> notifications = notificationService.getUnreadNotifications(user.getId());
            model.addAttribute("notifications", notifications);

            // take list order cua user
            List<PaymentLink> paymentLinks = orderService.getOrdersByUserId(user.getId());
//            for (PaymentLink paymentLink : paymentLinks) {
//                if (paymentLink.getCreatedAt() != null) {
//                    String formattedDate = formatDateTimeTo_ddMMyyyy(paymentLink.getCreatedAt());
//                    paymentLink.setTransactionDateFormatted(formattedDate);
//                }
//            }

// Đơn hàng chờ thanh toán
            List<PaymentLink> pendingPaymentLinks = orderService.getPendingOrdersByUserId(user.getId());


            // Đơn hàng đã thanh toán
            List<PaymentLink> paidPaymentLinks = orderService.getPaidOrdersByUserId(user.getId());

            // List voucher voi yeu thich
            List<Voucher> vouchers = IVoucherRepository.findAll();
            List<Favorite> favoriteList = favoriteService.getFavorites(user);
            System.out.println(favoriteList + "favoriteList.size(): " + favoriteList.size());

            model.addAttribute("user", user);
            model.addAttribute("paymentLinks", paymentLinks);
            model.addAttribute("pendingPaymentLinks", pendingPaymentLinks);
            model.addAttribute("paidPaymentLink", paidPaymentLinks);
            model.addAttribute("vouchers", vouchers);
            model.addAttribute("favoriteList", favoriteList);
            model.addAttribute("userDTO", new UserDTO());

            return "myAccount";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while loading the account page: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/contact")
    public String handleContactForm(@RequestParam String fullName,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String requestType,
                                    @RequestParam String message,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpSession session) {
        try {
            User user = (User) session.getAttribute("userLogin");
            if (user == null) {
                model.addAttribute("errorMessage", "Vui lòng đăng nhập để gửi khiếu nại hoặc góp ý.");
                model.addAttribute("userDTO", new UserDTO());
                return "contact";
            }

            // Kiểm tra xem da dat ve bao h chua
            boolean hasOrder = IPaymentLinkRepository.existsByUser_Id(user.getId());
            if (!hasOrder) {
                model.addAttribute("errorMessage", "Bạn cần từng đặt vé trước khi gửi khiếu nại hoặc góp ý.");
                model.addAttribute("userDTO", new UserDTO());
                return "contact";
            }

            // Tao ycau moi
            ContactRequest contactRequest = new ContactRequest();
            contactRequest.setFullName(fullName);
            contactRequest.setEmail(email);
            contactRequest.setPhone(phone);
            contactRequest.setRequestType(requestType);
            contactRequest.setMessage(message);
            contactRequest.setCreatedAt(LocalDateTime.now());
            contactRequest.setUser(user);

            IContactRequestRepository.save(contactRequest);
            System.out.println("Contact request saved successfully. CreatedAt: " + contactRequest.getCreatedAt());

            // gui lại ndung theo ycau
            String emailSubject;
            String emailBody;
            switch (requestType) {
                case "COMPLAINT":
                    emailSubject = "Xác Nhận Khiếu Nại";
                    emailBody = "Chào " + fullName + ",\n\n"
                            + "Chúng tôi đã nhận được khiếu nại của bạn. Nội dung khiếu nại:\n"
                            + message + "\n\n"
                            + "Chúng tôi sẽ xem xét và phản hồi bạn trong thời gian sớm nhất.\n"
                            + "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
                    break;
                case "FEEDBACK":
                    emailSubject = "Cảm Ơn Góp Ý Của Bạn";
                    emailBody = "Chào " + fullName + ",\n\n"
                            + "Cảm ơn bạn đã gửi góp ý cho chúng tôi. Nội dung góp ý:\n"
                            + message + "\n\n"
                            + "Chúng tôi rất trân trọng ý kiến của bạn và sẽ cải thiện dịch vụ tốt hơn.\n"
                            + "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
                    break;
                default:
                    emailSubject = "Xác Nhận Yêu Cầu";
                    emailBody = "Chào " + fullName + ",\n\n"
                            + "Chúng tôi đã nhận được yêu cầu của bạn:\n"
                            + message + "\n\n"
                            + "Chúng tôi sẽ phản hồi bạn trong thời gian sớm nhất.\n"
                            + "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
            }

            emailService.sendEmail(email, emailSubject, emailBody);
            System.out.println("Email sent to: " + email);

            model.addAttribute("successMessage", "Yêu cầu của bạn đã được gửi thành công! Vui lòng kiểm tra email để xem phản hồi.");
            model.addAttribute("userDTO", new UserDTO());
            return "contact";

        } catch (Exception e) {
            System.out.println("Error in handleContactForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra khi gửi yêu cầu: " + e.getMessage());
            model.addAttribute("userDTO", new UserDTO());
            return "contact";
        }
    }


    @GetMapping("/advertising-contact")
    public String advertisingContact(Model model, HttpSession session) {
        try {
            String securityCode = generateSecurityCode(6);
            session.setAttribute("securityCode", securityCode);
            model.addAttribute("securityCode", securityCode);
            model.addAttribute("userDTO", new UserDTO());
            System.out.println("Generated Security Code: " + securityCode);
            return "advertising-contact";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while loading the advertising contact page: " + e.getMessage());
            return "error";
        }
    }

}
