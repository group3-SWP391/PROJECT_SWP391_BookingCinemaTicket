package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface IUserRepository extends JpaRepository<User, Integer> {
    //Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.email = :email")
    List<User> findByEmailUserList(@Param("email") String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneAndPassword(String phone, String password);
    Optional<List<User>> findByFullnameContainingIgnoreCase(String keyword);
    Optional<User> findByPhone(String phone);
    List<User> findByUsername(String username);
    List<User> findAllByRole_Name(String roleName);
    List<User> findByStatusTrue();

}
