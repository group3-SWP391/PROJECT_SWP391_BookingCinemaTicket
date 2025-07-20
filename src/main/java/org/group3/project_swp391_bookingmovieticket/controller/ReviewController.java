package org.group3.project_swp391_bookingmovieticket.controller;

import jakarta.servlet.http.HttpSession;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Review;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.service.impl.MovieService;
import org.group3.project_swp391_bookingmovieticket.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReviewController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;
    @PostMapping("/submitReview")
    public String submitReview(
            @RequestParam("movieId") Integer movieId,  // đổi từ movieName
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để đánh giá.");
            return "redirect:/login";
        }

        Movie movie = movieService.findEntityById(movieId);
        if (movie == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phim.");
            return "redirect:/";
        }

        boolean hasOrdered = true;

        if (!hasOrdered) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần xem phim trước khi đánh giá.");
            return "redirect:/movie-detail?movieId=" + movieId;
        }

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setUser(user);
        review.setMovie(movie);
        review.setCreatedAt(LocalDateTime.now());

        reviewService.saveReview(review);

        return "redirect:/movie-details?id=" + movieId;

    }

}
