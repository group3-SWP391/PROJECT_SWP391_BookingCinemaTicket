package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;


public interface IPaymentLinkService extends IGeneralService<PaymentLink>{
    PaymentLink insert(PaymentLink paymentLink);
    void updateStatusByOrderCode(long orderCode, String newStatus);
    PaymentLink findByOrderCode(long orderCode);
}
