package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovieId(Integer movieId);
    Optional<Review> findByMovieIdAndUserId(Integer movieId, Integer userId);
    List<Review> findByMovieIdOrderByCreatedAtDesc(Integer movieId);

}
