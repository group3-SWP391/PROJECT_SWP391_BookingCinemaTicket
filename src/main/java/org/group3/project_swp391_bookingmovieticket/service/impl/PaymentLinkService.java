package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.*;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.service.IPaymentLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentLinkService implements IPaymentLinkService {

    @Autowired
    private IPaymentLinkRepository paymentLinkRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<PaymentLink> findAll() {
        return List.of();
    }

    @Override
    public Optional<PaymentLink> findById(Integer id) {
        return paymentLinkRepository.findById(id);
    }

    @Override
    public void update(PaymentLink paymentLink) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void save(PaymentLink paymentLink) {
        paymentLinkRepository.save(paymentLink);
    }

    @Override
    public void updateStatusByOrderCode(long orderCode, String newStatus) {
        PaymentLink paymentLink = paymentLinkRepository.findByOrderCode(orderCode);
        if (paymentLink != null) {
            paymentLink.setStatus(newStatus);
            paymentLinkRepository.save(paymentLink);
        }
    }

    @Override
    public PaymentLink findByOrderCode(long orderCode) {
        return paymentLinkRepository.findByOrderCode(orderCode);
    }

    @Override
    public boolean existsByOrderCodeAndStatus(long orderCode, String status) {
        return paymentLinkRepository.existsByOrderCodeAndStatus(orderCode, "PAID");
    }

    @Override
    public boolean existsBySchedule_IdAndSeatListAndStatus(int scheduleId, String id) {
        return paymentLinkRepository.existsBySchedule_IdAndSeatListAndStatus(scheduleId, id);
    }

    @Override
    public List<PaymentLink> getPaidOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserIdAndStatus(userId, "PAID");
    }

    @Override
    public List<PaymentLink> getOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserId(userId);
    }

    @Override
    public List<PaymentLink> getPendingOrdersByUserId(Integer userId) {
        return paymentLinkRepository.findByUserIdAndStatus(userId, "PENDING");
    }

    public void checkAndNotifyWatchedMoviesForUser(Integer userId) {
        List<PaymentLink> paidPaymentLinks = paymentLinkRepository.findByUserIdAndStatus(userId, "PAID");

        for (PaymentLink paymentLink : paidPaymentLinks) {

            Schedule schedule = paymentLink.getSchedule();
            if (schedule.getEndTime() == null || schedule.getEndTime() == null || schedule.getMovie() == null) continue;

            if (schedule.getEndTime().isBefore(LocalTime.now())) {
                Movie movie = schedule.getMovie();
                Notification existing = notificationService.getUnreadNotificationByUserAndMovie(userId, movie.getId());
                if (existing == null) {
                    notificationService.addNotification(paymentLink.getUser(), movie);
                }
            }
        }
    }

}
