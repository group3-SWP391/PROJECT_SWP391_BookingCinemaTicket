package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.group3.project_swp391_bookingmovieticket.dto.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dto.PopcornDrinkDTO;
import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.Seat;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.PaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.service.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.service.impl.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class CheckoutController {

    private final PayOS payOS;

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SeatService seatService;

    public CheckoutController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @PostMapping(value = "/check-duplicate-payment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkDuplicatePayment(@RequestBody BookingRequestDTO bookingRequestDTO, HttpServletRequest request) {
        System.out.println("Checking for duplicate payment link for schedule: " + bookingRequestDTO.getScheduleId());

        // Kiểm tra nếu đã có payment link trùng lặp
        boolean isDuplicate = paymentLinkService.existsBySchedule_IdAndSeatListAndStatus(
                bookingRequestDTO.getScheduleId(),
                seatService.findSeatNameById(bookingRequestDTO.getListSeatId().get(0))
        );
        System.out.println(isDuplicate + "status");

        if (isDuplicate) {
            // Nếu trùng lặp, trả về lỗi với thông báo
            System.out.println("Duplicate payment link found");
            return null; // Trả về false
        }

        // Nếu không có trùng lặp, gọi phương thức tạo payment link
        return checkout(bookingRequestDTO, request);
    }

    @PostMapping(value = "/create-payment-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> checkout(@RequestBody BookingRequestDTO bookingRequestDTO,
                                       HttpServletRequest request) {
        System.out.println("CheckoutController checkout" + bookingRequestDTO);

        request.getSession().setAttribute("bookingRequestDTO", bookingRequestDTO);
        Schedule schedule = scheduleService.findById(bookingRequestDTO.getScheduleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));


        List<String> listSeatName = new ArrayList<>();
        // Lấy thông tin ghế và giá cho từng ghế từ danh sách List<Integer>
        for (Integer seatId : bookingRequestDTO.getListSeatId()) {
            System.out.println(seatId);
            Seat seat = seatService.findById(seatId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));
            System.out.println(seat + "seat");
            String seatName = seat.getName();
            double seatPrice = seat.isVip() ? schedule.getPrice() + 20000 : schedule.getPrice(); // Cộng thêm tiền cho ghế VIP

            //Chuẩn hóa tiền có dạng xxx.xxx
            DecimalFormat formatter = new DecimalFormat("#,###");  // Dấu phân cách là dấu "."
            String formattedSeatPrice = formatter.format(seatPrice);

            if (seat.isVip()) {
                listSeatName.add(seatName + " (VIP, " + formattedSeatPrice + " VND)");
            } else {
                listSeatName.add(seatName + " (" + formattedSeatPrice + " VND)");
            }
        }

        //listSeatName cho nội dung bank
        List<String> listSeatNameBank = seatService.findSeatNamesByIdList(bookingRequestDTO.getListSeatId());


        // lấy về các bòng và nước để set vào payment link
        List<String> listPopcornDrinkName = new ArrayList<>();
        if (bookingRequestDTO.getListPopcornDrink() != null) {
            for (PopcornDrinkDTO dto : bookingRequestDTO.getListPopcornDrink()) {
                String nameWithQuantity = dto.getName() + " (x" + dto.getQuantity() + ")";
                listPopcornDrinkName.add(nameWithQuantity);
            }
        }
        try {
            final String baseUrl = getBaseUrl(request);
            final String productName = schedule.getMovie().getName();
            final String description = "Seat " + listSeatNameBank;
            final String returnUrl = baseUrl + "/bill/create_bill";
            final String cancelUrl = baseUrl + "/bill/cancel_bill";
            final int price = bookingRequestDTO.getTotalPrice();

            String currentTimeString = String.valueOf(System.currentTimeMillis());
            int randomPart = new Random().nextInt(900) + 100; // random 3 chữ số từ 100–999
            String combined = currentTimeString.substring(currentTimeString.length() - 6) + randomPart;
            long orderCode = Long.parseLong(combined);

            ItemData item = ItemData.builder()
                    .name(productName)
                    .price(price)
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(price)
                    .description(description)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            String checkoutUrl = data.getCheckoutUrl();

            // insert vào bảng để lưu
            PaymentLink paymentLink = new PaymentLink();
            paymentLink.setOrderCode(orderCode);
            paymentLink.setCheckoutUrl(checkoutUrl);
            paymentLink.setStatus("PENDING");
            paymentLink.setSchedule(schedule);
            paymentLink.setUser((User) request.getSession().getAttribute("userLogin"));
            paymentLink.setSeatList(String.join(",", listSeatName));
            paymentLink.setTotalPrice(price);
            paymentLink.setCreatedAt(LocalDateTime.now());
            if (bookingRequestDTO.getListPopcornDrink() != null) {
                paymentLink.setListPopcornDrinkName(String.join(",", listPopcornDrinkName));
            } else {
                paymentLink.setListPopcornDrinkName(null);
            }
            paymentLinkService.save(paymentLink);

            Map<String, String> response = new HashMap<>();
            response.put("checkoutUrl", checkoutUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when creating payment link.");
        }
    }


    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }
}