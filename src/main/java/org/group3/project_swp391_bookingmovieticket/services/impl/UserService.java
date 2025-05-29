package org.group3.project_swp391_bookingmovieticket.services.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);

    }



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

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public Optional<User> findByUsername(String username) {

        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {

        return Optional.empty();
    }

    @Override
    public Optional<User> findByPhoneAndPassword(String username, String password) {
        return userRepository.findByPhoneAndPassword(username, password);
    }
}
