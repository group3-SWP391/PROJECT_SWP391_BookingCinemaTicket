package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface IMovieService extends IGeneralService<MovieDTO> {

    Page<MovieDTO> findByMovieName(String movieName, Pageable pageable);

    List<String> getMovieCategories();

    Page<MovieDTO> findMovieComingSoon(Pageable pageable);

    Page<MovieDTO> getMovieByCategory(String categoryName, Pageable pageable);

    MovieDTO findMovieById(Integer id);

    Page<MovieDTO> findMovieNowShowing(Pageable pageable);

    List<MovieDTO> findMovieByViewDesc();

    Page<MovieDTO> findMovieByRatingId(Integer id, Pageable pageable);

    Page<MovieDTO> findByCategory(String category, Pageable pageable);

    Page<MovieDTO> findMovieByDirector(int directorId, Pageable pageable);

    Optional<Movie> findMovieEntityById(Integer id);

    void save (Movie movie);

    Optional<Movie> getMovieByName(String movieName);
}
