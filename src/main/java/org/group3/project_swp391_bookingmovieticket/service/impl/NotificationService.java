package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.NotificationDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Notification;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void addNotification(User user, Movie movie) {
        Notification notify = new Notification();
        notify.setUser(user);
        notify.setMovie(movie);
        notify.setMessage("Bạn vừa xem \"" + movie.getName() + "\". Hãy để lại đánh giá!");
        notify.setRead(false);
        notify.setCreatedAt(new Date());
        notificationRepository.save(notify);
    }

    public List<NotificationDTO> getUnreadNotifications(int userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(n -> new NotificationDTO(n.getMessage(), n.getMovie().getId()))
                .collect(Collectors.toList());
    }

    public Notification getUnreadNotificationByUserAndMovie(int userId, int movieId) {
        return notificationRepository.findFirstByUserIdAndMovieIdAndIsReadFalseOrderByCreatedAtDesc(userId, movieId);
    }
}
