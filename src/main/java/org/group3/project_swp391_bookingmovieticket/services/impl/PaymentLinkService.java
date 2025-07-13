package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.repositories.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.services.IPaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PaymentLinkService implements IPaymentLinkService {
    @Autowired
    private IPaymentLinkRepository iPaymentLinkRepository;
    @Override
    public void save(PaymentLink paymentLink) {
        iPaymentLinkRepository.save(paymentLink);


    }

    @Override
    public void updateStatusByOrderCode(long orderCode, String newStatus) {

    }

    @Override
    public PaymentLink findByOrderCode(long orderCode) {
        return iPaymentLinkRepository.findByOrderCode(orderCode);
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
        iPaymentLinkRepository.save(paymentLink);

    }

    @Override
    public void remove(Integer id) {

    }
}
