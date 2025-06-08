package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByPhoneAndPassword(String phone, String password);

    void saveTransactionHistory(Integer userId, Integer billId, String movieName, Integer seatId, Double price, String status);

    void changePassword(Integer userId, String newPassword);
}