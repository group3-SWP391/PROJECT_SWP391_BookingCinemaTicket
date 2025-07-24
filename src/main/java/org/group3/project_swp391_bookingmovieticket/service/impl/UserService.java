package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserByID(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<List<User>> findByUserNameIgnoreCase(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty.");
        }
        return userRepository.findByFullnameContainingIgnoreCase(keyword);
    }

    @Override
    public Page<User> getListUserPaging(int index) {
        int size = 6;
        Pageable pageable = PageRequest.of(index - 1, size);
        return userRepository.findAll(pageable);
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
    public void updateUserInfo(User user) {
        update(user);
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
        if (user == null) {
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
    public Optional<User> findByEmailAndPassword(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByPhoneAndPassword(username, password);
    }

    @Override
    public Optional<User> findByPhoneAndPassword(String username, String password) {
        return userRepository.findByPhoneAndPassword(username, password);
    }

    public boolean isEmailOrPhoneExists(User user) {
        Optional<User> byEmailOpt = userRepository.findByEmail(user.getEmail());
        Optional<User> byPhoneOpt = userRepository.findByPhone(user.getPhone());

        return (byEmailOpt.isPresent() && byEmailOpt.get().getId() != user.getId()) ||
                (byPhoneOpt.isPresent() && byPhoneOpt.get().getId() != user.getId());
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

    public void initiateUpdateProfile(User user) {
        otpService.generateOtp(user.getId(), user.getEmail(), "UPDATE_PROFILE");
    }


}
