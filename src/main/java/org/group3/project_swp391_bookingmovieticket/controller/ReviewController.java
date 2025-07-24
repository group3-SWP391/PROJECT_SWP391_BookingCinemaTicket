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
            @RequestParam("movieId") Integer movieId,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("userLogin");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn cần đăng nhập để đánh giá.");
            return "redirect:/login";
        }

        Movie movie = movieService.findEntityById(movieId);
        if (movie == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy phim.");
            return "redirect:/";
        }

        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setUser(user);
        review.setMovie(movie);
        // `createdAt` sẽ được set trong `reviewService.saveReview()`

        try {
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn đã đánh giá phim!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/movie/detail?movieId=" + movieId + "&tab=review";
    }


}
