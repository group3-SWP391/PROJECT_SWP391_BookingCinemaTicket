package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPaymentLinkRepository extends JpaRepository<PaymentLink, Integer> {
    List<PaymentLink> findAllByStatus(String status);
}
