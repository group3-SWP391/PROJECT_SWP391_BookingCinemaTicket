package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;

import java.util.List;
import java.util.Optional;

public interface IMovieService extends IGeneralService<MovieDTO> {
    List<MovieDTO> findAll();

    Optional<MovieDTO> findById(Integer id);

    void update(MovieDTO movieDTO);

    void remove(Integer id);

    List<MovieDTO> findByMovieName(String movieName);

    List<String> getMovieCategories();

    List<MovieDTO> getNowShowingMovies();

    List<MovieDTO> getCommingSoonMovies();

    List<MovieDTO> getMovieByCategory(String categoryName);
}