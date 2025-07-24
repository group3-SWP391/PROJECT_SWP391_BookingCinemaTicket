package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.PaymentLink;
import org.group3.project_swp391_bookingmovieticket.entity.Review;
import org.group3.project_swp391_bookingmovieticket.repository.IPaymentLinkRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private IReviewRepository reviewRepo;
    @Autowired
    private IPaymentLinkRepository IPaymentLinkRepository;

    public boolean hasUserReviewed(Integer userId, Integer movieId) {
        return reviewRepo.findByMovieIdAndUserId(movieId, userId).isPresent();
    }

    public void saveReview(Review review) {
        if (!canUserReview(review.getUser().getId(), review.getMovie().getName())) {
            throw new IllegalStateException("Bạn cần mua vé và xem phim để đánh giá.");
        }

        review.setCreatedAt(LocalDateTime.now());
        reviewRepo.save(review);
    }


    public List<Review> getReviewsForMovie(Integer movieId) {
        return reviewRepo.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public Page<Review> getReviewsForMovie(Integer movieId, Pageable pageable) {
        return reviewRepo.findByMovieId(movieId, pageable);
    }

    public boolean canUserReview(Integer userId, String movieName) {
        List<PaymentLink> paidLinks = IPaymentLinkRepository.findByUserIdAndStatus(userId, "PAID");
        LocalDateTime now = LocalDateTime.now();

        for (PaymentLink link : paidLinks) {
            if (link.getTickets() != null && link.getTickets().stream().anyMatch(ticket -> Boolean.TRUE.equals(ticket.getStatus()))) {
                return true;
            }
        }

        return false;
    }


}

