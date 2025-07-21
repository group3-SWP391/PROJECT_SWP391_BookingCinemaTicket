package org.group3.project_swp391_bookingmovieticket.service;


import org.group3.project_swp391_bookingmovieticket.entity.User;

import java.util.Optional;
import java.util.List;


public interface IUserService extends IGeneralService<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByPhoneAndPassword(String username, String password);

    Optional<User> findById(Integer id); // Dùng cho chức năng Edit nhân viên

    List<User> findAllByRoleName(String roleName);

}
