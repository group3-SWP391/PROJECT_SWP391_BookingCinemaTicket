package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.service.impl.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PromotionPageController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/promotion")
    public String showPromotionPage(Model model) {
        List<Voucher> vouchers = voucherService.getValidVouchers(null);
        model.addAttribute("vouchers", vouchers);
        return "promotion";
    }
}
