package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Movie;

import java.util.Optional;

public interface IMovieService extends IGeneralService<Movie>{
    Optional<Movie> getMovieByName(String movieName);
}
