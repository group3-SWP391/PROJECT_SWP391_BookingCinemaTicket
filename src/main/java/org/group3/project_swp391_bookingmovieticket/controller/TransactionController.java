package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entities.*;
import org.group3.project_swp391_bookingmovieticket.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class TransactionController {
    @Autowired
    private ITicketService iTicketService;
    @Autowired
    private ISeatService seatService;
    @Autowired
    private IPopcornDrinkService iPopcornDrinkService;
    @Autowired
    private IMovieService iMovieService;
    @Autowired
    private IBillService iBillService;
    @Autowired
    private IPaymentLinkService iPaymentLinkService;
    @GetMapping("/bill/create_bill")
    public String handlePayment(@RequestParam("orderCode") String orderCode, @RequestParam("status") String status, @RequestParam("cancel") String cancel, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession();
        //Từ orderCode check xem tình trạng sao để tránh giả mạo sau này.
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(Long.parseLong(orderCode));
        //Nếu tồn tại thì phair check cả status của nó nếu như nó còn là pending thì mới xử lý tiếp
        //Nếu như đã về trạng thái cancel rồi mà còn gửi giả mạo thanh toán thì từ chối xử lý
        if(paymentLink != null){
            if(paymentLink.getStatus().equalsIgnoreCase("PENDING")){
                paymentLink.setStatus("PAID");
                //tạo bill rồi update paymentLink một thể.
                Bill billInitial = new Bill();
                billInitial.setCreatedTime(LocalDateTime.now());
                billInitial.setUser((User)session.getAttribute("userLogin"));
                billInitial.setPrice(paymentLink.getTotalPrice());
                Bill bill = iBillService.save(billInitial);
                paymentLink.setBill(bill);
                iPaymentLinkService.update(paymentLink);

                //Tăng lượt view lên cho phim.
                Movie movie = paymentLink.getSchedule().getMovie();
                movie.setViews(movie.getViews() + 1);
                iMovieService.update(movie);
                //Trừ đi số lượng food.
                Map<String, String> listitems = (Map<String, String>)session.getAttribute("listitems");
                if(listitems == null){
                    return "redirect:/";
                }
                for (Map.Entry<String, String> entry : listitems.entrySet()){
                    String quantity = entry.getValue();
                    if(Integer.parseInt(quantity) > 0){
                        Optional<PopcornDrink> popcornDrinkOptional = iPopcornDrinkService.findById(Integer.parseInt(entry.getKey()));
                        if (popcornDrinkOptional.isPresent()){
                            PopcornDrink popcornDrink = popcornDrinkOptional.get();
                            popcornDrink.setQuantity(popcornDrink.getQuantity() - Integer.parseInt(quantity));
                            iPopcornDrinkService.update(popcornDrink);

                        }
                    }

                }
                //Lấy ra danh sách food để chèn vào cột popcorn_drink_list in Ticket
                String popcorn_drink = "";
                for (Map.Entry<String, String> entry : listitems.entrySet()){
                    String quantity = entry.getValue();
                    if(Integer.parseInt(quantity) > 0){
                        Optional<PopcornDrink> popcornDrink = iPopcornDrinkService.findById(Integer.parseInt(entry.getKey()));
                        if (popcornDrink.isPresent()){
                            popcorn_drink = popcorn_drink + popcornDrink.get().getName() + "(x"+quantity+")" + ",";
                        }
                    }

                }

                //Chèn vào trong bảng Ticket
                //Chọn bấy nhiêu ghế thì sẽ chèn bấy nhiêu vào bảng ticket.
                String selectedSeats = (String)session.getAttribute("listseat");
                List<Integer> seatIds = new ArrayList<>();
                String[] seatIdStrings = selectedSeats.split(",");

                for (String seatIdStr : seatIdStrings) {
                    seatIds.add(Integer.parseInt(seatIdStr));
                }
                List<Seat> seats = seatService.findSeatsByIds(seatIds);
                for(Seat seat: seats){
                    Ticket ticket = new Ticket();
                    //hard mã QR
                    ticket.setQrImageurl("/qrs/qr_bill.png");
                    ticket.setBill(bill);
                    ticket.setSchedule(paymentLink.getSchedule());
                    ticket.setSeat(seat);
                    ticket.setListPopcornDrinkName(popcorn_drink);
                    iTicketService.save(ticket);
                }
                redirectAttributes.addFlashAttribute("ordercode", orderCode);
                return "redirect:/successpayment";

            }else{
                //say goodbyte
                model.addAttribute("messageerror", "Giao dịch không hợp lệ hoặc đã bị hủy. Vui lòng thực hiện lại thanh toán nếu bạn muốn tiếp tục.");
                return "employee/error";
            }
         //Không tồn tại orderCode đó cũng say goodbyte
        }else{
            model.addAttribute("messageerror", "Không tồn tại orderCode để xử lý thanh toán");
        }
        return "employee/error";

    }
    @GetMapping("/successpayment")
    public String showPageSuccess(@ModelAttribute("ordercode") String orderCode, Model model){
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(Long.parseLong(orderCode));
        if(paymentLink != null){
            model.addAttribute("paymentLink", paymentLink);
        }
        return "employee/successpayment";



    }

    @GetMapping("/bill/cancel_screen")
    public String cancelPayment(@RequestParam("orderCode") String orderCode){
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(Long.parseLong(orderCode));
        if(paymentLink != null){
            paymentLink.setStatus("CANCELLED");
            iPaymentLinkService.update(paymentLink);
        }
        return "redirect:/cancelpayment";


    }
    @GetMapping("/cancelpayment")
    public String showPagePaymentFail(){
        return "employee/cancelpayment";
    }
}
