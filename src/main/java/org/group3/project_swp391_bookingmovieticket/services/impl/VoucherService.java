package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Voucher;
import org.group3.project_swp391_bookingmovieticket.repositories.OrderRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {
    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void generateVoucherForUser(Integer userId) {
        logger.debug("Generating voucher for userId: {}", userId);
        long orderCount = orderRepository.countByUserId(userId);
        long voucherCount = voucherRepository.countByUserId(userId);
        logger.debug("Order count: {}, Voucher count: {}", orderCount, voucherCount);

        if (orderCount == 1 && voucherCount == 0) {
            logger.info("Creating voucher for userId: {}", userId);
            String code = generateRandomVoucherCode();
            LocalDateTime now = LocalDateTime.now();

            Voucher voucher = new Voucher();
            voucher.setCode(code);
            voucher.setUserId(userId);
            voucher.setDiscountPercentage(10.0);
            voucher.setStartDate(now);
            voucher.setEndDate(now.plusDays(30));

            try {
                voucherRepository.save(voucher);
                logger.info("Voucher created successfully: {}", code);
            } catch (Exception e) {
                logger.error("Failed to save voucher for userId: {}. Error: {}", userId, e.getMessage());
            }
        } else {
            logger.debug("Voucher not created. Order count: {}, Voucher count: {}", orderCount, voucherCount);
        }
    }

    public List<Voucher> getValidVouchers(Integer userId) {
        logger.debug("Fetching valid vouchers for userId: {}", userId);
        return voucherRepository.findValidVouchersByUserId(userId, LocalDateTime.now());
    }

    public Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount) {
        logger.debug("Applying voucher {} for userId: {}", voucherCode, userId);
        List<Voucher> validVouchers = voucherRepository.findValidVouchersByUserId(userId, LocalDateTime.now());
        Voucher voucher = validVouchers.stream()
                .filter(v -> v.getCode().equals(voucherCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired voucher"));

        logger.info("Voucher {} applied successfully for userId: {}", voucherCode, userId);
        return voucher;
    }

    private String generateRandomVoucherCode() {
        return "VOUCHER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}