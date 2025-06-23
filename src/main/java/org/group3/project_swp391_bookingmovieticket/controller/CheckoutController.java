package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.dtos.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.services.impl.PaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.services.impl.ScheduleService;
import org.group3.project_swp391_bookingmovieticket.services.impl.SeatService;
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

import java.time.LocalDate;
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

    @RequestMapping(value = "/")
    public String Index() {
        return "index";
    }

    @RequestMapping(value = "/success")
    public String Success() {
        return "success";
    }

    @RequestMapping(value = "/cancel")
    public String Cancel() {
        return "cancel";
    }

    @PostMapping(value = "/create-payment-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkout(@RequestBody BookingRequestDTO bookingRequestDTO,
                                      HttpServletRequest request) {
        request.getSession().setAttribute("bookingRequestDTO", bookingRequestDTO);
        Schedule schedule = scheduleService.findById(bookingRequestDTO.getScheduleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        List<String> listSeatName = seatService.findSeatNamesByIdList(bookingRequestDTO.getListSeatId());

        try {
            final String baseUrl = getBaseUrl(request);
            final String productName = schedule.getMovie().getName();
            final String description = "Seat " + listSeatName;
            final String returnUrl = baseUrl + "/bill/create_bill";
            final String cancelUrl = baseUrl + "/bill/cancel_screen";
            final int price = 5000;

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