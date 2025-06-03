package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.dtos.DirectorDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDirectorRepository extends JpaRepository<DirectorDTO, Integer> {
    @Query("SELECT d FROM Movie m JOIN m.director d WHERE m.id = :movieId")
    DirectorDTO findDirectorByMovieId(@Param("movieId") Integer movieId);
}
