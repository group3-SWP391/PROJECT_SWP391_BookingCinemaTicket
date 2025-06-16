package org.group3.project_swp391_bookingmovieticket.services;


import org.group3.project_swp391_bookingmovieticket.entities.User;

import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
