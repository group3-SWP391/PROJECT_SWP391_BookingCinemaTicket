package org.group3.project_swp391_bookingmovieticket.service.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.dto.PaymentLinkDTO;
import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.repository.*;
import org.group3.project_swp391_bookingmovieticket.service.IPaymentLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentLinkService implements IPaymentLinkService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLinkService.class);

    @Autowired
    private PaymentLinkRepository paymentLinkRepository;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private IScheduleRepository scheduleRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<PaymentLink> getAllOrders() {
        return paymentLinkRepository.findAll();
    }

    @Override
    public Optional<PaymentLink> getOrderById(Integer id) {
        return paymentLinkRepository.findById(id);
    }

    @Override
    public List<PaymentLink> getPaidOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserIdAndStatus(userId, "PAID");
    }

    @Override
    @Transactional
    public PaymentLink createOrder(PaymentLinkDTO dto) {
        logger.info("Creating order for userId: {}", dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Bill bill = billRepository.findById(dto.getBillId())
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        Seat seat = seatRepository.findById(dto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        PaymentLink paymentLink = new PaymentLink(
                user,
                bill,
                dto.getMovieName(),
                seat,
                dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO,
                dto.getTransactionDate() != null ? dto.getTransactionDate() : LocalDateTime.now(),
                dto.getStatus()
        );

        PaymentLink savedPaymentLink = paymentLinkRepository.save(paymentLink);

        try {
            voucherService.generateVoucherForUser(user.getId());
            logger.info("Voucher generation attempted for userId: {}", user.getId());
        } catch (Exception e) {
            logger.error("Failed to generate voucher for userId: {}. Error: {}", user.getId(), e.getMessage(), e);
        }

        return savedPaymentLink;
    }

    @Override
    public PaymentLink updateOrder(Integer id, PaymentLinkDTO dto) {
        Optional<PaymentLink> optional = paymentLinkRepository.findById(id);
        if (optional.isPresent()) {
            PaymentLink paymentLink = optional.get();

            paymentLink.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
            paymentLink.setBill(billRepository.findById(dto.getBillId()).orElseThrow());
            paymentLink.setMovieName(dto.getMovieName());
            paymentLink.setSeat(seatRepository.findById(dto.getSeatId()).orElseThrow());

            paymentLink.setPrice(dto.getPrice() != null ? dto.getPrice() : paymentLink.getPrice());
            paymentLink.setTransactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : paymentLink.getTransactionDate());
            paymentLink.setStatus(dto.getStatus());

            return paymentLinkRepository.save(paymentLink);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (paymentLinkRepository.existsById(id)) {
            paymentLinkRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    @Override
    public List<PaymentLink> getOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserId(userId);
    }

    public PaymentLink saveOrder(PaymentLink paymentLink) {
        PaymentLink savedPaymentLink = paymentLinkRepository.save(paymentLink);
        voucherService.generateVoucherForUser(paymentLink.getUser().getId());
        return savedPaymentLink;
    }

    public void checkAndNotifyWatchedMoviesForUser(Integer userId) {
        List<PaymentLink> paidPaymentLinks = paymentLinkRepository.findByUserIdAndStatus(userId, "PAID");

        for (PaymentLink paymentLink : paidPaymentLinks) {

            Seat seat = paymentLink.getSeat();
            if (seat == null || seat.getSchedule() == null) continue;

            Schedule schedule = seat.getSchedule();
            if (schedule.getEndDate() == null || schedule.getEndTime() == null || schedule.getMovie() == null) continue;

            LocalDateTime endDateTime = LocalDateTime.of(schedule.getEndDate(), schedule.getEndTime());
            if (endDateTime.isBefore(LocalDateTime.now())) {
                Movie movie = schedule.getMovie();
                Notification existing = notificationService.getUnreadNotificationByUserAndMovie(userId, movie.getId());
                if (existing == null) {
                    notificationService.addNotification(paymentLink.getUser(), movie);
                }
            }
        }
    }

    public BigDecimal getTotalSpentInLast12Months(Integer userId) {
        List<PaymentLink> paidPaymentLinks = paymentLinkRepository.findByUserIdAndStatus(userId, "PAID");
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);

        return paidPaymentLinks.stream()
                .filter(paymentLink -> paymentLink.getTransactionDate() != null
                        && paymentLink.getTransactionDate().isAfter(twelveMonthsAgo)
                        && paymentLink.getPrice() != null)
                .map(PaymentLink::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public List<PaymentLink> getPendingOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserIdAndStatus(userId, "PENDING");
    }

}
