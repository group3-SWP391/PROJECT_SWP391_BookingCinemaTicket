package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Actor;
import org.group3.project_swp391_bookingmovieticket.entities.Director;
import org.group3.project_swp391_bookingmovieticket.entities.MovieActor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IMovieService extends IGeneralService<MovieDTO> {

    List<MovieDTO> findByMovieName(String movieName);

    List<String> getMovieCategories();

    List<MovieDTO> findMovieComingSoon();

    List<MovieDTO> getMovieByCategory(String categoryName);

    MovieDTO findMovieById(Integer id);

    List<MovieDTO> findMovieNowShowing();

    List<MovieDTO> findMovieByViewDesc();

    Page<MovieDTO> findByCategory(String category, Pageable pageable);

    Page<MovieDTO> findMovieByDirector(int directorId, Pageable pageable);
}
