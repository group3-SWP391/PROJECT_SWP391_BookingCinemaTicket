package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Review;
import org.group3.project_swp391_bookingmovieticket.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepo;

    public boolean hasUserReviewed(Integer userId, Integer movieId) {
        return reviewRepo.findByMovieIdAndUserId(movieId, userId).isPresent();
    }

    public void saveReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        reviewRepo.save(review);
    }

    public List<Review> getReviewsForMovie(Integer movieId) {
        return reviewRepo.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public Page<Review> getReviewsForMovie(Integer movieId, Pageable pageable) {
        return reviewRepo.findByMovieId(movieId, pageable);
    }

}

