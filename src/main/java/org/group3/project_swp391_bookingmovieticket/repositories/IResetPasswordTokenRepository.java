package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    ResetPasswordToken findByToken(String token);
    void deleteByUser_Id(Integer userId);
} 