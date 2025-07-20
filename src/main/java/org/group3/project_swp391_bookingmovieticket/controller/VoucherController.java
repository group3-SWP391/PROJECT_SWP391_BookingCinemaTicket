package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.service.impl.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<Voucher>> getValidVouchers(@RequestParam Integer userId) {
        if (userId == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Voucher> vouchers = voucherService.getValidVouchers(userId);
        return ResponseEntity.ok(vouchers);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyVoucher(
            @RequestParam("userId") Integer userId,
            @RequestParam("voucherCode") String voucherCode,
            @RequestParam("totalAmount") double totalAmount,
            @RequestParam("event") String event,
            @RequestParam("ticketType") String ticketType,
            @RequestParam("userType") String userType
    ) {
        try {
            Voucher voucher = voucherService.applyVoucher(userId, voucherCode, totalAmount, event, ticketType, userType);
            return ResponseEntity.ok(Map.of("voucher", voucher));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping("/info")
    public ResponseEntity<?> getVoucherInfo(@RequestParam("code") String code) {
        try {
            Voucher voucher = voucherService.getVoucherByCode(code);
            if (voucher == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Voucher không tồn tại."));
            }
            return ResponseEntity.ok(voucher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không thể tải thông tin voucher: " + e.getMessage()));
        }
    }

    private double calculateDiscount(Voucher voucher, double totalAmount) {
        if (voucher.getFixedDiscount() != null && voucher.getFixedDiscount() > 0) {
            return Math.min(voucher.getFixedDiscount(), totalAmount);
        } else if (voucher.getDiscountPercentage() != null && voucher.getDiscountPercentage() > 0) {
            return totalAmount * (voucher.getDiscountPercentage() / 100);
        }
        return 0;
    }
}