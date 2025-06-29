package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private OtpService otpService;

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
        try {
            String currentPassword = jdbcTemplate.queryForObject(
                    "SELECT password FROM [user] WHERE id = ?", String.class, user.getId());
            String sql = "UPDATE [user] SET full_name = ?, phone = ?, email = ?, password = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getFullname(), user.getPhone(), user.getEmail(), currentPassword, user.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
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
        String sql = "SELECT * FROM [user] WHERE email = ?";
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
        try {
            logger.debug("Checking username: {} and password: {}", username, password);
            Optional<User> userOptional = findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (password != null && password.equals(user.getPassword())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding user by username and password: {}", username, e);
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public void changePassword(Integer userId, String newPassword) {
        try {
            String sql = "UPDATE [user] SET password = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, newPassword, userId);
            if (rowsAffected == 0) {
                throw new RuntimeException("No user found with ID: " + userId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage(), e);
        }
    }

    public void initiateUpdateProfile(User user) {
        otpService.generateOtp(user.getId(), user.getEmail(), "UPDATE_PROFILE");
    }

    public boolean verifyUpdateProfile(Integer userId, String otpCode, User user) {
        try {
            if (otpService.verifyOtp(userId, otpCode, "UPDATE_PROFILE")) {
                update(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error verifying update profile for userId: {}", userId, e);
            return false;
        }
    }

    public void initiateChangePassword(Integer userId, String email) {
        otpService.generateOtp(userId, email, "CHANGE_PASSWORD");
    }

    public boolean verifyChangePassword(Integer userId, String otpCode, String newPassword) {
        try {
            if (otpService.verifyOtp(userId, otpCode, "CHANGE_PASSWORD")) {
                changePassword(userId, newPassword);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error verifying change password for userId: {}", userId, e);
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public void updateUserInfo(User user) {
        update(user);
    }

    public boolean isEmailOrPhoneExists(User user) {
        Optional<User> byEmailOpt = userRepository.findByEmail(user.getEmail());
        Optional<User> byPhoneOpt = userRepository.findByPhone(user.getPhone());

        return (byEmailOpt.isPresent() && !byEmailOpt.get().getId().equals(user.getId())) ||
                (byPhoneOpt.isPresent() && !byPhoneOpt.get().getId().equals(user.getId()));
    }


}
