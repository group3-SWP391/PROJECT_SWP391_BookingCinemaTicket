package org.group3.project_swp391_bookingmovieticket.controller;

import org.group3.project_swp391_bookingmovieticket.entities.Voucher;
import org.group3.project_swp391_bookingmovieticket.services.impl.VoucherService;
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
    public List<Voucher> getValidVouchers(@RequestParam Integer userId) {
        return voucherService.getValidVouchers(userId);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyVoucher(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String voucherCode = (String) request.get("voucherCode");
            Double totalAmount = ((Number) request.get("totalAmount")).doubleValue();
            Voucher voucher = voucherService.applyVoucher(userId, voucherCode, totalAmount);
            return ResponseEntity.ok(Map.of("voucher", voucher));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}