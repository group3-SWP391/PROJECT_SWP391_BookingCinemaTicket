package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.service.IPaymentLinkService;
import org.group3.project_swp391_bookingmovieticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/employee")
public class VerifyTicketController {
    @Autowired
    private ITicketService iTicketService;
    @Autowired
    private IPaymentLinkService iPaymentLinkService;
    @GetMapping("/verifyticket")
    public String showPageVerifyTicket(){
        return "employee/verifyticket";
    }
    @PostMapping("/verifyticket")
    public String proccessVerifyTicket(@RequestParam("ordercode") String orderCode, Model model){
        if(orderCode == null || orderCode.trim().isEmpty()){
            model.addAttribute("error", "Bạn chưa nhập mã code để tìm kiếm thông tin vé.");
            return "employee/verifyticket";
        }
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(Integer.parseInt(orderCode));
        if(paymentLink != null){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = LocalDateTime.of(paymentLink.getSchedule().getStartDate(), paymentLink.getSchedule().getEndTime());
            Integer id = paymentLink.getBill().getId();
            boolean check = iTicketService.verifyEffectiveOrderCode(id);
            //Vé đó đã được sử dụng rồi
            if(check){
                model.addAttribute("error", "Mã code này đã được sử dụng, xác nhận trước đó rồi!!!");
                model.addAttribute("key", orderCode);
                return "employee/verifyticket";


            //Vé đó đã qua thời gian chiếu của phim đó.
            }else if(now.isAfter(endTime)){
                model.addAttribute("error", "Đã qua thời gian sử dụng vé, vé không còn hiệu lực!!!");
                model.addAttribute("key", orderCode);
                return "employee/verifyticket";

            //Vé đó chưa được xác nhận lần nào
            }else{
                model.addAttribute("paymentlink", paymentLink);
                model.addAttribute("key", orderCode);
                return "employee/verifyticket";

            }





        }else{
            model.addAttribute("error", "Mã code bạn đang tìm kiếm không tồn tại");
            model.addAttribute("key", orderCode);
            return "employee/verifyticket";

        }
    }
    @PostMapping("/confirmation")
    public String showMessage(@RequestParam("idordercode") String id,   RedirectAttributes redirectAttributes){
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(Integer.parseInt(id));
        if(paymentLink != null){
            Integer idBill = paymentLink.getBill().getId();
            iTicketService.confirmTicket(idBill);
        }
        redirectAttributes.addFlashAttribute("ordercode", id);
        return "redirect:/employee/showinforticket";
    }
    @GetMapping("/showinforticket")
    public String showPrintTicketPage(@ModelAttribute("ordercode") Integer orderCode, Model model){
        PaymentLink paymentLink = iPaymentLinkService.findByOrderCode(orderCode);
        if(paymentLink != null){
            model.addAttribute("paymentLink", paymentLink);
        }
        return "employee/informationticket";

    }
}
