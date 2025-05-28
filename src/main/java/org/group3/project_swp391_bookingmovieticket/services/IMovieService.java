package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;

import java.util.List;


public interface IMovieService extends IGeneralService<MovieDTO>{

    List<MovieDTO> findByMovieName(String movieName);
<<<<<<< HEAD
    List<String> getMovieCategories();
    List<MovieDTO> getNowShowingMovies();
    List<MovieDTO> getCommingSoonMovies();
    List<MovieDTO> getMovieByCategory(String categoryName);
=======

>>>>>>> 076b1a6e2f871776af23ba40892fb6e79166c0b4
}
