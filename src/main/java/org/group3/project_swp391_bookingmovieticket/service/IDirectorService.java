package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.DirectorDTO;


public interface IDirectorService extends IGeneralService<DirectorDTO> {
    DirectorDTO findDirectorByMovieId(int movieId);

}
