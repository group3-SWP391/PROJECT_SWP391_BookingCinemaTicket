package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.UserDTO;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

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
        userRepository.save(user);
    }

    @Override
    public void remove(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty(); // không dùng username
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return Optional.empty(); // không dùng
    }

    @Override
    public Optional<User> findByPhoneAndPassword(String phone, String password) {
        return userRepository.findByPhoneAndPassword(phone, password);
    }

    @Override
    public List<User> findAllByRoleName(String roleName) {
        return userRepository.findAllByRole_Name(roleName);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    // === Các phương thức bổ sung cho profile ===

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setPhone(user.getPhone());
        dto.setFullname(user.getFullname());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    public void updateProfile(UserDTO dto) {
        Optional<User> optional = userRepository.findById(dto.getId());
        if (optional.isPresent()) {
            User user = optional.get();
            user.setFullname(dto.getFullname());
            user.setUsername(dto.getUsername());
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public boolean changePassword(String phone, String oldPassword, String newPassword) {
        Optional<User> optional = userRepository.findByPhone(phone);
        if (optional.isPresent()) {
            User user = optional.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
