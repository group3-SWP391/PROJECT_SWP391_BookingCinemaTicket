package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.service.IPaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PaymentLinkService implements IPaymentLinkService {
    @Autowired
    private IPaymentLinkRepository paymentLinkRepository;
    @Override
    public void save(PaymentLink paymentLink) {
        paymentLinkRepository.save(paymentLink);


    }

    @Override
    public void updateStatusByOrderCode(long orderCode, String newStatus) {

    }

    @Override
    public PaymentLink findByOrderCode(long orderCode) {

        return paymentLinkRepository.findByOrderCode(orderCode);
    }

    @Override
    public boolean existsByOrderCodeAndStatus(long orderCode, String status) {
        return false;
    }

    @Override
    public boolean existsBySchedule_IdAndSeatListAndStatus(int scheduleId, String id) {
        return false;
    }

    @Override
    public List<PaymentLink> findAll() {
        return List.of();
    }

    @Override
    public Optional<PaymentLink> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(PaymentLink paymentLink) {
        paymentLinkRepository.save(paymentLink);

    }

    @Override
    public void remove(Integer id) {

    }
}
