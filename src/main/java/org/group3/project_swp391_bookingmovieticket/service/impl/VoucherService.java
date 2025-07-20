package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.repository.PaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class VoucherService {
    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private PaymentLinkRepository paymentLinkRepository;

    public void generateVoucherForUser(Integer userId) {
        logger.debug("Generating voucher for userId: {}", userId);
        long orderCount = paymentLinkRepository.countByUserId(userId);
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
            voucher.setApplicableEvents("Summer2025,WELCOME,VIP100K");
            voucher.setApplicableTicketTypes("VIP,Regular");
            voucher.setApplicableUserTypes("MEMBER");
            voucher.setFixedDiscount(0.0);
            voucher.setMinOrderValue(150000.0);
            voucher.setMaxUsageCount(100);
            voucher.setCurrentUsageCount(0);
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
        if (userId == 0) {
            logger.warn("Invalid userId: 0. Returning empty voucher list.");
            return List.of();
        }
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

    public Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount, String event, String ticketType, String userType) {
        logger.debug("Applying voucher {} for userId: {}", voucherCode, userId);

        // Kiểm tra theo các điều kiện voucher
        Voucher voucher = voucherRepository.findByCode(voucherCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid voucher code"));

        if (voucher.isUsed() || voucher.getCurrentUsageCount() >= voucher.getMaxUsageCount()) {
            throw new IllegalArgumentException("Voucher has been used or reached usage limit");
        }

        LocalDateTime now = LocalDateTime.now();
        if (voucher.getEndDate().isBefore(now) || voucher.getStartDate().isAfter(now)) {
            throw new IllegalArgumentException("Voucher is expired or not yet valid");
        }
        logger.debug("userType={}, applicableUserTypes={}", userType, voucher.getApplicableUserTypes());
        if (voucher.getApplicableUserTypes() != null && !voucher.getApplicableUserTypes().isBlank()) {
            List<String> userTypes = List.of(voucher.getApplicableUserTypes().split(","));
            if (userTypes.stream().noneMatch(u -> u.trim().equalsIgnoreCase(userType))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho loại người dùng này");
            }
        }
        logger.debug("event={}, applicableEvents={}", event, voucher.getApplicableEvents());
        if (voucher.getApplicableEvents() != null && !voucher.getApplicableEvents().isBlank()) {
            List<String> events = List.of(voucher.getApplicableEvents().split(","));
            if (events.stream().noneMatch(e -> e.trim().equalsIgnoreCase(event))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho sự kiện này");
            }
        }
        logger.debug("ticketType={}, applicableTicketTypes={}", ticketType, voucher.getApplicableTicketTypes());
        if (voucher.getApplicableTicketTypes() != null && !voucher.getApplicableTicketTypes().isBlank()) {
            List<String> ticketTypes = List.of(voucher.getApplicableTicketTypes().split(","));
            if (ticketTypes.stream().noneMatch(t -> t.trim().equalsIgnoreCase(ticketType))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho loại vé này");
            }
        }


        if (totalAmount < voucher.getMinOrderValue()) {
            throw new IllegalArgumentException("Order value does not meet minimum requirement");
        }

        if (voucher.getUserId() != null && !voucher.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Voucher is not valid for this user");
        }

        if ((voucher.getDiscountPercentage() == null || voucher.getDiscountPercentage() == 0.0) &&
                (voucher.getFixedDiscount() == null || voucher.getFixedDiscount() == 0.0)) {
            throw new IllegalArgumentException("Voucher has no valid discount value");
        }

        voucher.setCurrentUsageCount(voucher.getCurrentUsageCount() + 1);
        if (voucher.getMaxUsageCount() == 1) {
            voucher.setUsed(true);
        }

        try {
            voucherRepository.save(voucher);
            logger.info("Voucher {} applied successfully for userId: {}", voucherCode, userId);
        } catch (Exception e) {
            logger.error("Failed to save voucher update for code: {}. Error: {}", voucherCode, e.getMessage());
            throw new RuntimeException("Failed to apply voucher: " + e.getMessage());
        }

        return voucher;
    }


    public Voucher getVoucherByCode(String code) {
        logger.debug("Fetching voucher by code: {}", code);
        return voucherRepository.findByCode(code).orElse(null);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Voucher not found with ID: " + id));
    }

}