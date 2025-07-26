package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.MovieActorDTO;

import java.util.List;

public interface IMovieActorService extends IGeneralService<MovieActorDTO>{
    List<MovieActorDTO> findAllActorByMovieId(int movieId);


}
