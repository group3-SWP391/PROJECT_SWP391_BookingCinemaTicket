package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieActorDTO;

import java.util.List;

public interface IMovieActorService extends IGeneralService<MovieActorDTO>{
    List<MovieActorDTO> findAllActorByMovieId(int movieId);


}
