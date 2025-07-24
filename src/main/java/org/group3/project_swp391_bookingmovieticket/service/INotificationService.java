package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.User;

public interface INotificationService {
    // Thêm thông báo khi người dùng xem một bộ phim
    void addNotification(User user, Movie movie);

    // Gửi thông báo nếu người dùng chưa xem phim sau 15 phút
    void sendNotificationIfNotWatched(User user, Movie movie);
}
