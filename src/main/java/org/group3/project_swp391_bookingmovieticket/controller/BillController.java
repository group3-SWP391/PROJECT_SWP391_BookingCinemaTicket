package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.constant.CommonConst;
import org.group3.project_swp391_bookingmovieticket.dto.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dto.PopcornDrinkDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.dto.UserRegisterDTO;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.service.TicketEmailService;
import org.group3.project_swp391_bookingmovieticket.service.impl.*;
import org.group3.project_swp391_bookingmovieticket.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.group3.project_swp391_bookingmovieticket.constant.CommonConst.USER_REGISTER_DTO;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private BillService billService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TicketEmailService ticketEmailService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PopcornDrinkService popcornDrinkService;

    @GetMapping("/create_bill")
    public String createBill(@RequestParam("orderCode") long orderCode,
                             @RequestParam("status") String status,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {


        // 1. Nếu bill đã được xử lý trước đó → chuyển thẳng đến confirmation screen
        if (paymentLinkService.existsByOrderCodeAndStatus(orderCode, "PAID")) {
            redirectAttributes.addAttribute("orderCode", orderCode);
            return "redirect:/bill/confirmation_screen";
        }

        // 2. Chỉ xử lý tạo bill nếu status từ PayOS là PAID
        PaymentLink paymentLink = null;
        if ("PAID".equals(status)) {
            BookingRequestDTO dto = (BookingRequestDTO) request.getSession().getAttribute("bookingRequestDTO");

            // Tăng lượt view của phim khi người dùng thanh toán thành công
            Optional<Movie> movieOpt = movieService.findMovieEntityById(dto.getMovieId());
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                movie.setViews(movie.getViews() + 1);
                movieService.save(movie);
            }

            // Tạo bill và cập nhật trạng thái thanh toán
            Bill bill = billService.createNewBill(dto);
            paymentLink = paymentLinkService.findByOrderCode(orderCode);
            paymentLink.setBill(bill);
            paymentLink.setStatus(status);
            paymentLinkService.save(paymentLink);

            // Xử lý trừ đi số lượng mua bỏng và nước
            if (dto.getListPopcornDrink() != null) {
                for (PopcornDrinkDTO popcornDrinkDTO : dto.getListPopcornDrink()) {
                    // Lấy về popcornDrink trong db
                    PopcornDrink popcornDrink = popcornDrinkService.findById(popcornDrinkDTO.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Popcorn Drink not found"));
                    if (popcornDrink != null) {
                        popcornDrink.setQuantity(popcornDrink.getQuantity() - popcornDrinkDTO.getQuantity());
                        popcornDrinkService.save(popcornDrink);
                    }
                }
            } else {
                System.out.println("<UNK> <UNK> <UNK> <UNK> <UNK> <UNK> <UNK> <UNK> <UNK>");
            }

            try {
                User user = (User) request.getSession().getAttribute("userLogin");
                List<Ticket> tickets = bill.getTickets();

                if (!tickets.isEmpty()) {
                    // tícket nào cũng như nhau nếu cùng bill
                    Ticket sampleTicket = tickets.get(0);

                    String customerName = user.getFullname();
                    String toEmail = user.getEmail();
                    String movieName = sampleTicket.getSchedule().getMovie().getName();
                    String showDate = sampleTicket.getSchedule().getStartDate().toString();
                    String showTime = sampleTicket.getSchedule().getStartTime().toString();
                    String branchName = sampleTicket.getSchedule().getBranch().getName();
                    String movieFormat = sampleTicket.getSchedule().getRoom().getRoomType();
                    String listPopcornAndDrinkName = sampleTicket.getListPopcornDrinkName();
                    String seatNames = tickets.stream()
                            .map(t -> {
                                String name = t.getSeat().getName();
                                boolean isVip = t.getSeat().isVip(); // giả sử có phương thức isVip()
                                return isVip ? name + " (VIP)" : name;
                            })
                            .collect(Collectors.joining(", "));

                    // Tạo QR code
                    String qrContent = "localhost:8080/ticket-detail?movieId="
                            + sampleTicket.getSchedule().getMovie().getId()
                            + "&ticketId="
                            + sampleTicket.getId()
                            + "&orderCode="
                            + orderCode;
                    byte[] qrBytes = QRCodeGenerator.generateQRCodeAsBytes(qrContent, 1000, 1000);
                    String qrFileName = "qr_bill_" + bill.getId() + ".png";
                    String qrImageUrl = QRCodeGenerator.saveQrToLocal(qrBytes, qrFileName);

                    // Cập nhật QR vào vé
                    tickets.forEach(t -> t.setQrImageURL(qrImageUrl));
                    ticketService.saveAll(tickets);

                    // Gửi email vé
                    ticketEmailService.sendTicketWithQr(
                            toEmail,
                            "🎟️ Your Movie Ticket Confirmation",
                            customerName,
                            movieName,
                            showDate,
                            showTime,
                            branchName,
                            seatNames,
                            movieFormat,
                            listPopcornAndDrinkName,
                            qrBytes
                    );
                }
            } catch (Exception e) {
                e.printStackTrace(); // Có thể thay bằng logger nếu cần
            }
        }

        // 3. Luôn redirect về màn hình xác nhận
        redirectAttributes.addAttribute("orderCode", orderCode);
        return "redirect:/bill/confirmation_screen";
    }


    @GetMapping("/confirmation_screen")
    public String confirmationScreen(@RequestParam("orderCode") long orderCode,
                                     HttpServletRequest request,
                                     Model model) {
        BookingRequestDTO bookingRequestDTO = (BookingRequestDTO) request.getSession().getAttribute("bookingRequestDTO");
        if (bookingRequestDTO == null) {
            return "redirect:/home";
        }
        Optional<Schedule> scheduleOpt = scheduleService.findById(bookingRequestDTO.getScheduleId());
        scheduleOpt.ifPresent(schedule -> model.addAttribute("scheduleOrder", schedule));
        model.addAttribute("paymentLink", paymentLinkService.findByOrderCode(orderCode));
        model.addAttribute("qrTicket", ticketService.findTicketsByOrderCode(orderCode).get(0));
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "confirmation_screen";
    }

    @GetMapping("/cancel_bill")
    public String cancelBill(@RequestParam("orderCode") long orderCode,
                             @RequestParam("status") String status) {
        paymentLinkService.updateStatusByOrderCode(orderCode, status);
        return "redirect:/bill/cancel_screen";
    }

    @GetMapping("/cancel_screen")
    public String cancelScreen(Model model) {
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return "cancel_screen";
    }
}
