package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.DirectorDTO;


public interface IDirectorService extends IGeneralService<DirectorDTO> {
    DirectorDTO findDirectorByMovieId(int movieId);

}
