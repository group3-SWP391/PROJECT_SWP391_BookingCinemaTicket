package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPaymentLinkRepository extends JpaRepository<PaymentLink, Integer> {
    List<PaymentLink> findAllByStatus(String status);

    PaymentLink findByOrderCode(long orderCode);

    boolean existsByOrderCodeAndStatus(long orderCode, String status);

}
