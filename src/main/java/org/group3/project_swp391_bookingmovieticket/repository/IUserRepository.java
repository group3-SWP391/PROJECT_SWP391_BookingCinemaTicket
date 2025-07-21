package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneAndPassword(String phone, String password);
    Optional<List<User>> findByFullnameContainingIgnoreCase(String keyword);

}
