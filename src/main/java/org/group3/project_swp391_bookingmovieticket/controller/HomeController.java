package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.repositories.*;
import org.group3.project_swp391_bookingmovieticket.services.ICommentReactionService;
import org.group3.project_swp391_bookingmovieticket.services.ICommentService;
import org.group3.project_swp391_bookingmovieticket.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MovieService movieService;

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

    @GetMapping
    public String displayHomePage(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("movieAllLargeImageURL", movieService.findAll());
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("movieAll", movieService.findAll());
        model.addAttribute("movieByCategory", movieService.findAll());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "about";
    }

    @GetMapping("/blog-category")
    public String blogCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_category";
    }

    @GetMapping("/blog-single")
    public String blogSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_single";
    }

    @GetMapping("/booking-type")
    public String bookingType(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "booking_type";
    }

    @GetMapping("/confirmation-screen")
    public String confirmationScreen(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "confirmation_screen";
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

    @GetMapping("/event-category")
    public String eventCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_category";
    }

    @GetMapping("/event-single")
    public String eventSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_single";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "gallery";
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

    @GetMapping("/movie-single")
    public String movieSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single";
    }

    @GetMapping("/movie-single-second")
    public String movieSingleSecond(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single_second";
    }

    @GetMapping("/movie-details")
    public String movieDetails(@RequestParam("id") Integer movieId, Model model, HttpSession session) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalMovie.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy phim với ID: " + movieId);
            return "error"; // trang error.html hoặc redirect về /movie-category
        }

        Movie movie = optionalMovie.get();
        List<Comment> comments = commentService.getCommentsByMovieId(movieId);
        Map<Integer, Long> likesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countLikes(c)));
        Map<Integer, Long> dislikesMap = comments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> reactionService.countDislikes(c)));

        model.addAttribute("movie", movie);
        model.addAttribute("comments", comments);
        model.addAttribute("likesMap", likesMap);
        model.addAttribute("dislikesMap", dislikesMap);
        User user = (User) session.getAttribute("userLogin");
        boolean hasOrdered = false;
        boolean isFavorite = false;

        if (user != null) {
            hasOrdered = orderRepository.existsByUser_IdAndMovieName(user.getId(), movie.getName());
        }if (user != null) {
            isFavorite = favoriteService.isFavorite(user, movie);
        }
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("hasOrdered", hasOrdered);


        return "movie-details";
    }


    @GetMapping("/voucher/{id}")
    public String viewVoucherDetail(@PathVariable Integer id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "voucher_detail";
    }

    @GetMapping("/my-account")
    public String myAccount(Model model, HttpSession session) {
        try {
            User user = (User) session.getAttribute("userLogin");
            if (user == null) {
                model.addAttribute("error", "Please log in to view your account.");
                return "redirect:/login";
            }

            System.out.println("User loaded in myAccount: " + user.getFullname() + ", " + user.getPhone() + ", " + user.getEmail());

            List<Order> orders;
            try {
                orders = orderService.getOrdersByUserId(user.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (Order order : orders) {
                    order.setTransactionDateFormatted(order.getTransactionDate().format(formatter));
                }
            } catch (Exception e) {
                model.addAttribute("error", "Failed to load order history: " + e.getMessage());
                orders = List.of();
            }

            List<Voucher> vouchers;
            try {
                vouchers = voucherRepository.findAll();
            } catch (Exception e) {
                model.addAttribute("error", "Failed to load vouchers: " + e.getMessage());
                vouchers = List.of();
            }
            List<Favorite> favoriteList;
            try {
                favoriteList = favoriteService.getFavorites(user);
            } catch (Exception e) {
                model.addAttribute("error", "Failed to load favorite list: " + e.getMessage());
                favoriteList = List.of();
            }

            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
            model.addAttribute("vouchers", vouchers);
            model.addAttribute("userDTO", new UserDTO());
            model.addAttribute("favoriteList", favoriteService.getFavorites(user));

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
                                    Model model) {
        try {
            ContactRequest contactRequest = new ContactRequest();
            contactRequest.setFullName(fullName);
            contactRequest.setEmail(email);
            contactRequest.setPhone(phone);
            contactRequest.setRequestType(requestType);
            contactRequest.setMessage(message);
            contactRequest.setCreatedAt(LocalDateTime.now());
            contactRequestRepository.save(contactRequest);
            System.out.println("Contact request saved successfully. CreatedAt: " + contactRequest.getCreatedAt());

            String emailSubject;
            String emailBody;
            switch (requestType) {
                case "BOOKING":
                    emailSubject = "Xác Nhận Yêu Cầu Đặt Vé Xem Phim";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được yêu cầu đặt vé xem phim của bạn. Nội dung yêu cầu:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ liên hệ lại với bạn sớm nhất qua số điện thoại " + phone + ".\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
                    break;
                case "COMPLAINT":
                    emailSubject = "Xác Nhận Khiếu Nại";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được khiếu nại của bạn. Nội dung khiếu nại:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ xem xét và phản hồi bạn trong thời gian sớm nhất.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
                    break;
                case "FEEDBACK":
                    emailSubject = "Cảm Ơn Góp Ý Của Bạn";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Cảm ơn bạn đã gửi góp ý cho chúng tôi. Nội dung góp ý:\n" +
                            message + "\n\n" +
                            "Chúng tôi rất trân trọng ý kiến của bạn và sẽ cải thiện dịch vụ tốt hơn.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
                    break;
                default:
                    emailSubject = "Xác Nhận Yêu Cầu";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được yêu cầu của bạn. Nội dung:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ phản hồi bạn sớm nhất có thể.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim FPT Cinemas cảm ơn!!!";
            }

            emailService.sendEmail(email, emailSubject, emailBody);
            System.out.println("Email sent to: " + email);

            model.addAttribute("successMessage", "Yêu cầu của bạn đã được gửi thành công!!!!!!! Vui lòng kiểm tra email để xem phản hồi.");
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