package org.group3.project_swp391_bookingmovieticket.service;



import org.group3.project_swp391_bookingmovieticket.dto.PaymentLinkDTO;
import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;

import java.util.List;
import java.util.Optional;

public interface IPaymentLinkService {
    List<PaymentLink> getAllOrders();
    Optional<PaymentLink> getOrderById(Integer id);

    List<PaymentLink> getPaidOrdersByUserId(Integer userId);

    PaymentLink createOrder(PaymentLinkDTO paymentLinkDTO);
    PaymentLink updateOrder(Integer id, PaymentLinkDTO paymentLinkDTO);
    void deleteOrder(Integer id);
    List<PaymentLink> getOrdersByUserId(Integer userId);

    List<PaymentLink> getPendingOrdersByUserId(Integer userId);
}