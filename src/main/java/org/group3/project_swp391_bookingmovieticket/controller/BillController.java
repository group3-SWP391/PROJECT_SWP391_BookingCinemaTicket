package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.constant.CommonConst;
import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.UserLoginDTO;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.TicketEmailService;
import org.group3.project_swp391_bookingmovieticket.services.impl.BillService;
import org.group3.project_swp391_bookingmovieticket.services.impl.PaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.TicketService;
import org.group3.project_swp391_bookingmovieticket.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private BillService billService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TicketEmailService ticketEmailService;

    @Autowired
    private TicketService ticketService;

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

            // Tạo bill và cập nhật trạng thái thanh toán
            Bill bill = billService.createNewBill(dto);
            paymentLink = paymentLinkService.findByOrderCode(orderCode);
            paymentLink.setBill(bill);
            paymentLink.setStatus(status);
            paymentLinkService.save(paymentLink);

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
                    String movieFormat = sampleTicket.getSchedule().getMovie().getFormat();
                    String seatNames = tickets.stream()
                            .map(t -> t.getSeat().getName())
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
        System.out.println("orderCode: " + orderCode);
        BookingRequestDTO bookingRequestDTO = (BookingRequestDTO) request.getSession().getAttribute("bookingRequestDTO");
        if (bookingRequestDTO == null) {
            System.out.println("bookingRequestDTO is null");
            return "redirect:/home";
        }
        Optional<Schedule> scheduleOpt = scheduleService.findById(bookingRequestDTO.getScheduleId());
        scheduleOpt.ifPresent(schedule -> model.addAttribute("scheduleOrder", schedule));
        model.addAttribute("paymentLink", paymentLinkService.findByOrderCode(orderCode));
        System.out.println("=============================");
        model.addAttribute("qrTicket", ticketService.findTicketsByOrderCode(orderCode));
        System.out.println("QR URL = " + ticketService.findTicketsByOrderCode(orderCode).getQrImageURL());
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        return "confirmation_screen";
    }

    @GetMapping("/cancel_screen")
    public String cancelScreen(@RequestParam("orderCode") long orderCode,
                               @RequestParam("status") String status,
                               Model model) {
        paymentLinkService.updateStatusByOrderCode(orderCode, status);
        model.addAttribute(CommonConst.USER_LOGIN_DTO, new UserLoginDTO());
        return "cancel_screen";
    }
}
