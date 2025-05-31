package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entities.ContactRequest;
import org.group3.project_swp391_bookingmovieticket.repositories.ContactRequestRepository;
import org.group3.project_swp391_bookingmovieticket.services.impl.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.group3.project_swp391_bookingmovieticket.services.impl.EmailService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @GetMapping
    public String displayHomePage(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("movieAllLargeImageURL", movieService.findAll());
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("movieAll", movieService.findAll());
        model.addAttribute("movieByCategory", movieService.findAll());
        model.addAttribute("userDTO", new UserDTO());
        return "home";
    }

    // Trang About
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "about";
    }

    // Trang Blog Category
    @GetMapping("/blog-category")
    public String blogCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_category";
    }

    // Trang Blog Single
    @GetMapping("/blog-single")
    public String blogSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "blog_single";
    }

    // Trang Booking Type
    @GetMapping("/booking-type")
    public String bookingType(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "booking_type";
    }

    // Trang Confirmation Screen
    @GetMapping("/confirmation-screen")
    public String confirmationScreen(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "confirmation_screen";
    }

    // Trang Contact
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

    // Trang Event Category
    @GetMapping("/event-category")
    public String eventCategory(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_category";
    }

    // Trang Event Single
    @GetMapping("/event-single")
    public String eventSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "event_single";
    }

    // Trang Gallery
    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "gallery";
    }

    // Trang Movie Booking
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

        // Add other required attributes
        model.addAttribute("categoryAll", movieService.getMovieCategories());
        model.addAttribute("userDTO", new UserDTO());

        // Return the Thymeleaf template name
        return "movie_category";
    }


    // Trang Movie Single
    @GetMapping("/movie-single")
    public String movieSingle(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single";
    }

    // Trang Movie Single Second
    @GetMapping("/movie-single-second")
    public String movieSingleSecond(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "movie_single_second";
    }

    // Trang Seat Booking
    @GetMapping("/seat-booking")
    public String seatBooking(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "seat_booking";
    }

    // Trang Movie Details
    @GetMapping("/movie-details")
    public String movieDetails(@RequestParam("id") int id, Model model) {
        Optional<MovieDTO> movieOptional = movieService.findById(id);
        model.addAttribute("movie", movieOptional.get());
        model.addAttribute("userDTO", new UserDTO());
        return "movie_details";
    }
    @GetMapping("/test-email")
    public String testEmail(Model model) {
        try {
            emailService.sendEmail(
                    "sonledz22cm@gmail.com",
                    "Test Email from Spring Boot",
                    "This is a test email sent from your Spring Boot application!"
            );
            model.addAttribute("successMessage", "Test email sent successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to send test email: " + e.getMessage());
        }
        return "contact";
    }

    // Xử lý biểu mẫu Liên Hệ
    @PostMapping("/contact")
    public String handleContactForm(@RequestParam String fullName,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String requestType,
                                    @RequestParam String message,
                                    Model model) {
        try {
            // Lưu thông tin liên hệ vào cơ sở dữ liệu
            ContactRequest contactRequest = new ContactRequest();
            contactRequest.setFullName(fullName);
            contactRequest.setEmail(email);
            contactRequest.setPhone(phone);
            contactRequest.setRequestType(requestType);
            contactRequest.setMessage(message);
            contactRequestRepository.save(contactRequest);
            System.out.println("Before saving - createdAt: " + contactRequest.getCreatedAt());
            contactRequest.setCreatedAt(LocalDateTime.now());

            // Gửi email phản hồi tự động
            String emailSubject;
            String emailBody;
            switch (requestType) {
                case "BOOKING":
                    emailSubject = "Xác Nhận Yêu Cầu Đặt Vé Xem Phim";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được yêu cầu đặt vé xem phim của bạn. Nội dung yêu cầu:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ liên hệ lại với bạn sớm nhất qua số điện thoại " + phone + ".\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim";
                    break;
                case "COMPLAINT":
                    emailSubject = "Xác Nhận Khiếu Nại";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được khiếu nại của bạn. Nội dung khiếu nại:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ xem xét và phản hồi bạn trong thời gian sớm nhất.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim";
                    break;
                case "FEEDBACK":
                    emailSubject = "Cảm Ơn Góp Ý Của Bạn";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Cảm ơn bạn đã gửi góp ý cho chúng tôi. Nội dung góp ý:\n" +
                            message + "\n\n" +
                            "Chúng tôi rất trân trọng ý kiến của bạn và sẽ cải thiện dịch vụ tốt hơn.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim";
                    break;
                default:
                    emailSubject = "Xác Nhận Yêu Cầu";
                    emailBody = "Chào " + fullName + ",\n\n" +
                            "Chúng tôi đã nhận được yêu cầu của bạn. Nội dung:\n" +
                            message + "\n\n" +
                            "Chúng tôi sẽ phản hồi bạn sớm nhất có thể.\n" +
                            "Trân trọng,\nHệ thống đặt vé xem phim";
            }

            emailService.sendEmail(email, emailSubject, emailBody);

            // Hiển thị thông báo thành công
            model.addAttribute("successMessage", "Yêu cầu của bạn đã được gửi thành công! Vui lòng kiểm tra email để xem phản hồi.");
            model.addAttribute("userDTO", new UserDTO());
            return "contact";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra khi gửi yêu cầu: " + e.getMessage());
            model.addAttribute("userDTO", new UserDTO());
            return "contact";
        }
    }

}
