package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserIdAndOtpCodeAndActionTypeAndIsUsedFalse(Integer userId, String otpCode, String actionType);
}