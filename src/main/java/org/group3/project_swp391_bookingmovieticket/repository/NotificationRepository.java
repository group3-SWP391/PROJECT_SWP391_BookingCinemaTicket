package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(int userId);
    Notification findFirstByUserIdAndMovieIdAndIsReadFalseOrderByCreatedAtDesc(int userId, int movieId);
    List<Notification> findAllByUserIdAndMovieIdAndIsReadFalse(int userId, int movieId);

}
