package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or user ID cannot be null.");
        }
        // Lấy password hiện tại từ database
        String currentPassword = jdbcTemplate.queryForObject(
                "SELECT password FROM [user] WHERE id = ?", String.class, user.getId());
        String sql = "UPDATE [user] SET full_name = ?, phone = ?, email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getFullname(), user.getPhone(), user.getEmail(), currentPassword, user.getId());
    }

    @Override
    public void remove(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        String sql = "DELETE FROM [user] WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM [user] WHERE email = ?"; // Giả sử username là email
        try {
            Map<String, Object> userMap = jdbcTemplate.queryForMap(sql, username);
            User user = new User();
            user.setId(((Number) userMap.get("id")).intValue());
            user.setFullname((String) userMap.get("full_name"));
            user.setPhone((String) userMap.get("phone"));
            user.setEmail((String) userMap.get("email"));
            user.setPassword((String) userMap.get("password"));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) { // So sánh trực tiếp
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByPhoneAndPassword(String phone, String password) {
        System.out.println("Searching for phone: " + phone + ", password: " + password);
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Found user with phone: " + user.getPhone() + ", password: " + user.getPassword());
            if (password.equals(user.getPassword())) {
                System.out.println("Password match for userId: " + user.getId());
                return Optional.of(user);
            } else {
                System.out.println("Password mismatch");
            }
        } else {
            System.out.println("No user found with phone: " + phone);
        }
        return Optional.empty();
    }

    @Override
    public void saveTransactionHistory(Integer userId, Integer billId, String movieName, Integer seatId, Double price, String status) {
        String sql = "INSERT INTO transaction_history (user_id, bill_id, movie_name, seat_id, price, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, billId, movieName, seatId, price, status);
    }

    @Override
    public void changePassword(Integer userId, String newPassword) {
        String sql = "UPDATE [user] SET password = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newPassword, userId);
        System.out.println("Rows affected by password update: " + rowsAffected + " for userId: " + userId);
        if (rowsAffected == 0) {
            System.out.println("No rows updated - check userId or database constraints.");
        }
    }
}