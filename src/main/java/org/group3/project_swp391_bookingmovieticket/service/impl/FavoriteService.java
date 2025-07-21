package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Favorite;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.User;
import org.group3.project_swp391_bookingmovieticket.repository.IFavoriteRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private IFavoriteRepository IFavoriteRepository;

    @Autowired
    private IMovieRepository movieRepository;

    public void toggleFavorite(User user, Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));

        Optional<Favorite> existing = IFavoriteRepository.findByUserAndMovie(user, movie);
        if (existing.isPresent()) {
            IFavoriteRepository.delete(existing.get());
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setMovie(movie);
            favorite.setCreatedAt(LocalDateTime.now());
            IFavoriteRepository.save(favorite);
        }
    }

    public boolean isFavorite(User user, Movie movie) {
        return IFavoriteRepository.existsByUserAndMovie(user, movie);
    }

    public List<Favorite> getFavorites(User user) {
        return IFavoriteRepository.findAllByUser(user);
    }
}

