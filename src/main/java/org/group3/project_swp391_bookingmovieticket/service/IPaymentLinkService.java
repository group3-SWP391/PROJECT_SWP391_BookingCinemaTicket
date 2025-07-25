package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;

import java.util.List;
import java.util.Optional;


public interface IPaymentLinkService extends IGeneralService<PaymentLink>{
    void save(PaymentLink paymentLink);
    void updateStatusByOrderCode(long orderCode, String newStatus);
    PaymentLink findByOrderCode(long orderCode);
    boolean existsByOrderCodeAndStatus(long orderCode, String status);
    boolean existsBySchedule_IdAndSeatListAndStatus(int scheduleId, String id);



    List<PaymentLink> getPaidOrdersByUserId(Integer userId);
    List<PaymentLink> getOrdersByUserId(Integer userId);
    List<PaymentLink> getPendingOrdersByUserId(Integer userId);



}