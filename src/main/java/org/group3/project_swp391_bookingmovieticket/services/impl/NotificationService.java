package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.NotificationDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.entities.Notification;
import org.group3.project_swp391_bookingmovieticket.entities.User;
import org.group3.project_swp391_bookingmovieticket.repositories.NotificationRepository;
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
