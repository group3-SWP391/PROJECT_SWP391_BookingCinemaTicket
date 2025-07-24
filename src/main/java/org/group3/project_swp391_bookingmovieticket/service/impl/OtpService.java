package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Otp;
import org.group3.project_swp391_bookingmovieticket.repository.IOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private IOtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;

    public String generateOtp(Integer userId, String email, String actionType) {
        String otpCode = generateRandomOtp();

        long otpCount = otpRepository.count();
        if (otpCount >= 20) {
            Optional<Otp> oldestOtp = otpRepository.findTopByOrderByCreatedAtAsc();
            oldestOtp.ifPresent(otpRepository::delete);
        }

        Otp otp = new Otp();
        otp.setUserId(userId);
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setIsUsed(false);
        otp.setActionType(actionType);

        otpRepository.save(otp);

        emailService.sendEmail(email, "Your OTP Code", "Your OTP: " + otpCode);
        return otpCode;
    }


    public boolean verifyOtp(Integer userId, String otpCode, String actionType) {
        logger.debug("Verifying OTP: {} for userId: {}, action: {}", otpCode, userId, actionType);
        Optional<Otp> otpOptional = otpRepository.findByUserIdAndOtpCodeAndActionTypeAndIsUsedFalse(userId, otpCode, actionType);
        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            logger.debug("Found OTP, expires at: {}", otp.getExpiresAt());
            if (otp.getExpiresAt().isAfter(LocalDateTime.now())) {
                otp.setIsUsed(true);
                otpRepository.save(otp);
                logger.debug("OTP verified successfully");
                return true;
            } else {
                logger.debug("OTP expired");
            }
        } else {
            logger.debug("No valid OTP found");
        }
        return false;
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}