package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;

import java.util.Optional;

public interface IMovieService extends IGeneralService<Movie>{
    Optional<Movie> getMovieByName(String movieName);
}
