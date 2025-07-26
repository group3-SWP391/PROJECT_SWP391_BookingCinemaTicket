package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.entity.Voucher;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IVoucherRepository;
import org.group3.project_swp391_bookingmovieticket.service.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VoucherService implements IVoucherService {
    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    @Autowired
    private IVoucherRepository voucherRepository;

    @Autowired
    private IPaymentLinkRepository paymentLinkRepository;

    @Override
    public List<Voucher> getValidVouchers(Integer userId) {
        logger.debug("Fetching valid vouchers (shared for all users)");
        return voucherRepository.findValidVouchers(LocalDateTime.now());
    }

    @Override
    public Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount) {
        logger.debug("Applying voucher {} for userId: {}", voucherCode, userId);
        List<Voucher> validVouchers = voucherRepository.findValidVouchers(LocalDateTime.now());

        Voucher voucher = validVouchers.stream()
                .filter(v -> v.getCode().equalsIgnoreCase(voucherCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired voucher"));

        logger.info("Voucher {} applied successfully", voucherCode);
        return voucher;
    }

    @Override
    public Voucher applyVoucher(Integer userId, String voucherCode, double totalAmount, String event, String ticketType, String userType) {
        logger.debug("Applying voucher {} with event={}, ticketType={}, userType={}", voucherCode, event, ticketType, userType);

        Voucher voucher = voucherRepository.findByCode(voucherCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid voucher code"));

        LocalDateTime now = LocalDateTime.now();
        if (voucher.isUsed() || voucher.getCurrentUsageCount() >= voucher.getMaxUsageCount()) {
            throw new IllegalArgumentException("Voucher has been used or reached usage limit");
        }

        if (voucher.getEndDate().isBefore(now) || voucher.getStartDate().isAfter(now)) {
            throw new IllegalArgumentException("Voucher is expired or not yet valid");
        }

        if (voucher.getApplicableUserTypes() != null && !voucher.getApplicableUserTypes().isBlank()) {
            List<String> userTypes = List.of(voucher.getApplicableUserTypes().split(","));
            if (userTypes.stream().noneMatch(u -> u.trim().equalsIgnoreCase(userType))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho loại người dùng này");
            }
        }

        if (voucher.getApplicableEvents() != null && !voucher.getApplicableEvents().isBlank()) {
            List<String> events = List.of(voucher.getApplicableEvents().split(","));
            if (events.stream().noneMatch(e -> e.trim().equalsIgnoreCase(event))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho sự kiện này");
            }
        }

        if (voucher.getApplicableTicketTypes() != null && !voucher.getApplicableTicketTypes().isBlank()) {
            List<String> ticketTypes = List.of(voucher.getApplicableTicketTypes().split(","));
            if (ticketTypes.stream().noneMatch(t -> t.trim().equalsIgnoreCase(ticketType))) {
                throw new IllegalArgumentException("Voucher không áp dụng cho loại vé này");
            }
        }

        if (totalAmount < voucher.getMinOrderValue()) {
            throw new IllegalArgumentException("Giá trị đơn hàng không đủ điều kiện");
        }

        if ((voucher.getDiscountPercentage() == null || voucher.getDiscountPercentage() == 0.0) &&
                (voucher.getFixedDiscount() == null || voucher.getFixedDiscount() == 0.0)) {
            throw new IllegalArgumentException("Voucher không có giá trị giảm hợp lệ");
        }

        voucher.setCurrentUsageCount(voucher.getCurrentUsageCount() + 1);
        if (voucher.getMaxUsageCount() == 1) {
            voucher.setUsed(true);
        }

        try {
            voucherRepository.save(voucher);
            logger.info("Voucher {} saved after applying", voucherCode);
        } catch (Exception e) {
            logger.error("Failed to update voucher {}. Error: {}", voucherCode, e.getMessage());
            throw new RuntimeException("Lỗi khi áp dụng voucher");
        }

        return voucher;
    }

    @Override
    public Voucher getVoucherByCode(String code) {
        return voucherRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Voucher not found with ID: " + id));
    }

    @Override
    public List<Voucher> getVouchersByUser(User user) {
        return voucherRepository.findValidVouchers(LocalDateTime.now());
    }

    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }
}
