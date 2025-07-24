package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Favorite;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndMovie(User user, Movie movie);
    List<Favorite> findAllByUser(User user);
    boolean existsByUserAndMovie(User user, Movie movie);
    void deleteByUserAndMovie(User user, Movie movie);
}

