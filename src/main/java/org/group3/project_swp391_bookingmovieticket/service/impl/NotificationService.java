package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.NotificationDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Notification;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.INotificationRepository;
import org.group3.project_swp391_bookingmovieticket.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private INotificationRepository INotificationRepository;

    public void addNotification(User user, Movie movie) {
        Notification notify = new Notification();
        notify.setUser(user);
        notify.setMovie(movie);
        notify.setMessage("Bạn vừa xem \"" + movie.getName() + "\". Hãy để lại đánh giá!");
        notify.setRead(false);
        notify.setCreatedAt(new Date());
        INotificationRepository.save(notify);
    }
//add timer add notification neu dat ve nhung ko xem
    public List<NotificationDTO> getUnreadNotifications(int userId) {
        List<Notification> notifications = INotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(n -> new NotificationDTO(n.getMessage(), n.getMovie().getId()))
                .collect(Collectors.toList());
    }

    public Notification getUnreadNotificationByUserAndMovie(int userId, int movieId) {
        return INotificationRepository.findFirstByUserIdAndMovieIdAndIsReadFalseOrderByCreatedAtDesc(userId, movieId);
    }

    public void sendNotificationIfNotWatched(User user, Movie movie) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMovie(movie);
        notification.setMessage("Bạn chưa xem phim \"" + movie.getName() + "\" trong thời gian quy định. Nếu bạn gặp vấn đề gì hãy liên hệ với chúng tôi.");
        notification.setRead(false);
        notification.setCreatedAt(new Date());
        INotificationRepository.save(notification);
    }

    public List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(int userId){
        return INotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public List<Notification> findAllByUserIdAndMovieIdAndIsReadFalse(int userId, int movieId){
        return INotificationRepository.findAllByUserIdAndMovieIdAndIsReadFalse(userId, movieId);
    }

    public void saveAll(List<Notification> notifications) {
        INotificationRepository.saveAll(notifications);
    }
}
