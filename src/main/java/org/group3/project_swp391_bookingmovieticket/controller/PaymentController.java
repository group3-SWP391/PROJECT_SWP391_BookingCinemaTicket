package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.service.IPaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.service.IPopcornDrinkService;
import org.group3.project_swp391_bookingmovieticket.service.ISeatService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.group3.project_swp391_bookingmovieticket.service.impl.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PaymentController {
    @Autowired
    private PayOS payOS;
    @Autowired
    private ITicketService ticketService;

    @Autowired
    private ISeatService seatService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private IPopcornDrinkService popcornDrinkService;

    @Autowired
    private IPaymentLinkService paymentLinkService;

    @PostMapping("/checkout")
    public String handleCheckout(HttpServletRequest request, Model model, @RequestParam Map<String, String> items, RedirectAttributes redirectAttributes)
                                  {
        HttpSession session = request.getSession();
        String selectedSeats = (String)session.getAttribute("listseat");
        Integer scheduleId = (Integer)session.getAttribute("schedulemovie");

        //Lưu items vào session để xử lý sau khi payos callback lại còn trừ đi
                                      session.setAttribute("listitems", items);

        Double totalPriceFood = 0.0;
        String popcorn_drink = "";
        for (Map.Entry<String, String> entry : items.entrySet()){
            String quantity = entry.getValue();
            if(Integer.parseInt(quantity) > 0){
                Optional<PopcornDrink> popcornDrink = popcornDrinkService.findById(Integer.parseInt(entry.getKey()));
                if (popcornDrink.isPresent()){
                    totalPriceFood += popcornDrink.get().getPrice();
                    popcorn_drink = popcorn_drink + popcornDrink.get().getName() + "(x"+quantity+")" + ",";
                }
            }

        }
        session.setAttribute("totalPriceFood", totalPriceFood);
        

        List<Integer> seatIds = new ArrayList<>();
        String[] seatIdStrings = selectedSeats.split(",");

        for (String seatIdStr : seatIdStrings) {
            seatIds.add(Integer.parseInt(seatIdStr));
        }

        Optional<Schedule> optionalSchedule = scheduleService.findById(scheduleId);
        if (optionalSchedule.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy lịch chiếu!");
            return "redirect:/"; // hoặc về lại trang chọn lịch
        }

        // Lấy danh sách ghế từ những cái ghế người dùng chọn.
        List<Seat> seats = seatService.findSeatsByIds(seatIds);

        // Lấy danh sách ghế đã đặt trước đó để đối chiếu với ghế người dùng đã chọn xem có trùng ko
        HashSet<Integer> bookedSeatIds = ticketService.findBookedSeatIdsBySchedule(scheduleId);
        System.out.println(bookedSeatIds);

        // Nếu có bất kỳ ghế nào trùng thì báo lỗi
        if (seatIds.stream().map(Integer::intValue).anyMatch(bookedSeatIds::contains)) {
            model.addAttribute("messageerror", "Rất tiếc! Ghế bạn chọn đã được đặt bởi người khác. Vui lòng chọn ghế khác.");
            return "employee/error";
        }


        String seatList = "";
        for(int i = 0; i < seats.size(); i++){
            if(!seats.get(i).getIsVip() && i != seats.size() - 1){
                seatList = seatList  + seats.get(i).getName() + "(" + (optionalSchedule.get().getPrice()) +"VND"+")"+",";
            } else if (seats.get(i).getIsVip() && i != seats.size() - 1) {
                seatList = seatList + seats.get(i).getName() + "(" + (optionalSchedule.get().getPrice() + 20000)+" VND"+")"+",";

            }else{
                if(!seats.get(i).getIsVip()){
                    seatList = seatList  + seats.get(i).getName() + "(" + (optionalSchedule.get().getPrice())+" VND"+")";

                }else{
                    seatList = seatList + seats.get(i).getName() + "(" + (optionalSchedule.get().getPrice() + 20000)+" VND"+")";

                }

            }
        }


        // Tính tổng tiền Ghế.
        double basePrice = optionalSchedule.get().getPrice();
        double totalSeat = 0;
        for (Seat seat : seats) {
            totalSeat += basePrice + (seat.getIsVip() ? 20000 : 0);
        }

        //Tổng tiền ghế + food.
        double total = totalSeat + totalPriceFood;


        String currentTimeString = String.valueOf(System.currentTimeMillis());
        int randomPart = new Random().nextInt(900) + 100; // random 3 chữ số từ 100–999
        String combined = currentTimeString.substring(currentTimeString.length() - 6) + randomPart;
        long orderCode = Long.parseLong(combined);

        String baseUrl = getBaseUrl(request);
        String returnUrl = baseUrl + "/staff/createbill";
        String cancelUrl = baseUrl + "/staff/cancelbill";

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount((int)total)
                .description("Thanh toán vé xem phim")
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl).build();
        try {
            CheckoutResponseData responseData = payOS.createPaymentLink(paymentData);
            //Tạo một payment_link
            PaymentLink paymentLink = new PaymentLink();
            paymentLink.setOrderCode(orderCode);
            paymentLink.setCheckoutUrl(responseData.getCheckoutUrl());
            paymentLink.setStatus("PENDING");
            paymentLink.setUser((User)session.getAttribute("userLogin"));
            paymentLink.setSchedule(optionalSchedule.get());
            paymentLink.setSeatList(seatList);
            paymentLink.setTotalPrice((int)total);
            paymentLink.setCreatedAt(LocalDateTime.now());
            paymentLink.setListPopcornDrinkName(popcorn_drink);
            paymentLinkService.save(paymentLink);
            return "redirect:"+responseData.getCheckoutUrl();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("messageerror", "Có lỗi trong quá trình thanh toán");
        return "employee/error";


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
