package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;

import java.util.List;


public interface IMovieService extends IGeneralService<MovieDTO>{

    List<MovieDTO> findByMovieName(String movieName);

}
