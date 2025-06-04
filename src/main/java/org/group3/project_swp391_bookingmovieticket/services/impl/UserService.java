package org.group3.project_swp391_bookingmovieticket.services.impl;

import jakarta.transaction.Transactional;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IUserRepository;
import org.group3.project_swp391_bookingmovieticket.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IBillRepository iBillRepository;

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);

    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
             if(iBillRepository.existsById(id)){
                 throw new IllegalArgumentException("Cannot delete because they have existing bills.");

             }else{
                 userRepository.deleteById(id);
             }

        }else{
            throw new IllegalArgumentException(" With id "+ id + " not found");
        }



    }

    @Override
    public Optional<User> getUserByID(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user;
        }
        throw new IllegalArgumentException(" With id "+ id + " not found");



    }

    @Override
    public Optional<List<User>> findByUserNameIgnoreCase(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty.");
        }
        return userRepository.findByFullnameContainingIgnoreCase(keyword);



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
