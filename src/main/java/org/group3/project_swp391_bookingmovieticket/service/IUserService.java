package org.group3.project_swp391_bookingmovieticket.service;


import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    Optional<User> findByEmailAndPassword(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByPhoneAndPassword(String username, String password);

    void save (User user);

    void delete(int id);

    Optional<User> getUserByID(int id);

    Optional<List<User>> findByUserNameIgnoreCase(String keyword);

    Page<User> getListUserPaging(int index);





}
