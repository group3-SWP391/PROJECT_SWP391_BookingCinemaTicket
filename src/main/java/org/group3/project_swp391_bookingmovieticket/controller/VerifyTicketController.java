package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.services.IPaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee")
public class VerifyTicketController {
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
            model.addAttribute("paymentlink", paymentLink);
            model.addAttribute("key", orderCode);
            return "employee/verifyticket";

        }else{
            model.addAttribute("error", "Mã code bạn đang tìm kiếm không tồn tại");
            model.addAttribute("key", orderCode);
            return "employee/verifyticket";

        }



    }
}
