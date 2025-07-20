package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovieId(Integer movieId);
    Optional<Review> findByMovieIdAndUserId(Integer movieId, Integer userId);
    List<Review> findByMovieIdOrderByCreatedAtDesc(Integer movieId);
    Page<Review> findByMovieId(Integer movieId, Pageable pageable);
}
