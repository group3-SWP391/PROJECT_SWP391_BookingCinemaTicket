package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Notification;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/read/{movieId}")
    public String markNotificationAsReadAndRedirect(@PathVariable("movieId") int movieId,
                                                    HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null) return "redirect:/login";

        List<Notification> notifications = notificationRepository
                .findAllByUserIdAndMovieIdAndIsReadFalse(user.getId(), movieId);

        for (Notification n : notifications) {
            n.setRead(true);
        }
        notificationRepository.saveAll(notifications);


        return "redirect:/movie-details?id=" + movieId;
    }
}

