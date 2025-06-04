package org.group3.project_swp391_bookingmovieticket.services;


import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByPhoneAndPassword(String username, String password);

    void save (User user);

    void delete(int id);

    Optional<User> getUserByID(int id);

    Optional<List<User>> findByUserNameIgnoreCase(String keyword);





}
