package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.NotificationDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.services.ICommentReactionService;
import org.group3.project_swp391_bookingmovieticket.services.ICommentService;
import org.group3.project_swp391_bookingmovieticket.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private AdvertisingContactRequestRepository advertisingContactRequestRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ICommentReactionService reactionService;

    @GetMapping({"/", "/home"})
    public String showHomePage(HttpSession session, Model model, HttpServletRequest request) {
        User user = (User) session.getAttribute("userLogin");

        if (user == null) {
            // Nếu chưa đăng nhập, không hiển thị thông báo
            model.addAttribute("notifications", Collections.emptyList());
        } else {
            // Lấy thông báo chưa đọc từ DB
            List<Notification> notifications = notificationRepository
                    .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());

            // Chuyển sang DTO để hiển thị gọn trong giao diện
            List<NotificationDTO> notificationDTOs = notifications.stream()
                    .map(n -> new NotificationDTO(n.getMessage(), n.getMovie().getId()))
                    .collect(Collectors.toList());

            model.addAttribute("notifications", notificationDTOs);
            System.out.println("DEBUG: notifications size = " + notificationDTOs.size());
        }

        // ⚠️ Đảm bảo luôn có dữ liệu cần thiết cho trang chủ
        request.getSession().setAttribute("movieAllLargeImageURL", movieService.findAll());
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("movieAll", movieService.findAll());
        model.addAttribute("movieByCategory", movieService.findAll());
        model.addAttribute("userDTO", new UserDTO());

        return "home";
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

    @GetMapping("/movie-booking")
    public String movieBooking(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_booking";
    }

    @GetMapping("/movie-category")
    public String displayMovieCategory(@RequestParam(value = "categoryName", required = false) String categoryName, Model model) {
        if (categoryName == null || categoryName.trim().isEmpty() || categoryName.equalsIgnoreCase("movieAll")) {
            model.addAttribute("movieByCategory", movieService.findAll());
        } else {
            model.addAttribute("movieByCategory", movieService.getMovieByCategory(categoryName));
        }
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("userDTO", new UserDTO());
        return "movie_category";
    }


    @GetMapping("/movie-details")
    public String movieDetails(@RequestParam("id") Integer movieId,
                               @RequestParam(value = "commentPage", defaultValue = "0") int commentPage,
                               @RequestParam(value = "reviewPage", defaultValue = "0") int reviewPage,
                               Model model,
                               HttpSession session) {

        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalMovie.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy phim với ID: " + movieId);
            return "error"; // hoặc redirect về trang chính
        }

        Movie movie = optionalMovie.get();

        // Phân trang
        Pageable commentPageable = PageRequest.of(commentPage, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable reviewPageable = PageRequest.of(reviewPage, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Comment> commentPageResult = commentService.getCommentsByMovieId(movieId, commentPageable);
        Page<Review> reviewPageResult = reviewService.getReviewsForMovie(movieId, reviewPageable);

        // lay ra list bluan va danh gia
        List<Comment> comments = commentPageResult.getContent();
        List<Review> reviews = reviewPageResult.getContent();

        // Like/Dislike
        Map<Integer, Long> likesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countLikes(c)));

        Map<Integer, Long> dislikesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countDislikes(c)));

        // Gán dữ liệu vào model
        model.addAttribute("movie", movie);
        model.addAttribute("comments", comments);
        model.addAttribute("reviews", reviews);
        model.addAttribute("commentPage", commentPageResult);
        model.addAttribute("reviewPage", reviewPageResult);
        model.addAttribute("likesMap", likesMap);
        model.addAttribute("dislikesMap", dislikesMap);

        // Kiểm tra người dùng đã đặt vé hoặc đã yêu thích
        User user = (User) session.getAttribute("userLogin");
        boolean hasOrdered = false;
        boolean isFavorite = false;

        if (user != null) {
            hasOrdered = orderRepository.existsByUser_IdAndMovieName(user.getId(), movie.getName());
            isFavorite = favoriteService.isFavorite(user, movie);
        }

        model.addAttribute("hasOrdered", hasOrdered);
        model.addAttribute("isFavorite", isFavorite);

        return "movie-details";
    }

    @GetMapping("/voucher/{id}")
    public String viewVoucherDetail(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "voucher_detail";
    }

    private String getMemberLevel(BigDecimal totalSpent) {
        if (totalSpent.compareTo(BigDecimal.valueOf(5_000_000)) >= 0) return "Kim cương";
        if (totalSpent.compareTo(BigDecimal.valueOf(3_000_000)) >= 0) return "Vàng";
        if (totalSpent.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) return "Bạc";
        return "Đồng";
    }

    @GetMapping("/my-account")
    public String myAccount(Model model, HttpSession session) {
        try {
            User user = (User) session.getAttribute("userLogin");
            if (user == null) {
                model.addAttribute("error", "Please log in to view your account.");
                return "redirect:/login";
            }

            List<NotificationDTO> notifications = notificationService.getUnreadNotifications(user.getId());
            model.addAttribute("notifications", notifications);

            // take list order cua user
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Order order : orders) {
                if (order.getTransactionDate() != null) {
                    order.setTransactionDateFormatted(order.getTransactionDate().format(formatter));
                }
            }

            // Đơn hàng chờ thanh toán
            List<Order> pendingOrders = orders.stream()
                    .filter(o -> "PENDING".equalsIgnoreCase(o.getStatus()))
                    .toList();

            // Đơn hàng đã thanh toán
            List<Order> paidOrders = orderService.getPaidOrdersByUserId(user.getId());

            // Tổng chi tiêu trong 12 tháng gần nhất
            BigDecimal totalSpent = orderService.getTotalSpentInLast12Months(user.getId());
            String memberLevel = getMemberLevel(totalSpent);

            // Lấy danh sách voucher và phim yêu thích
            List<Voucher> vouchers = voucherRepository.findAll();
            List<Favorite> favoriteList = favoriteService.getFavorites(user);

            // Đẩy dữ liệu ra view
            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("paidOrders", paidOrders);
            model.addAttribute("vouchers", vouchers);
            model.addAttribute("favoriteList", favoriteList);
            model.addAttribute("userDTO", new UserDTO());
            model.addAttribute("memberLevel", memberLevel);

            return "myAccount";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while loading the account page: " + e.getMessage());
            return "error";
        }
    }

    private static final String SESSION_USER_LOGIN = "userLogin";


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
            // Kiểm tra đăng nhập
            User user = (User) session.getAttribute(SESSION_USER_LOGIN);
            if (user == null) {
                model.addAttribute("errorMessage", "Vui lòng đăng nhập để gửi khiếu nại hoặc góp ý.");
                model.addAttribute("userDTO", new UserDTO());
                return "contact";
            }

            // Kiểm tra đã từng đặt vé
            boolean hasOrder = orderRepository.existsByUser_Id(user.getId());
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

            contactRequestRepository.save(contactRequest);
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
            advertisingContactRequestRepository.save(advertisingContactRequest);
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

    private String generateSecurityCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
}