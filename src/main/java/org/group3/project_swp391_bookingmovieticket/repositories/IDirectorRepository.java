package org.group3.project_swp391_bookingmovieticket.repositories;


import org.group3.project_swp391_bookingmovieticket.entities.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDirectorRepository extends JpaRepository<Director, Integer> {
    @Query("SELECT d FROM Movie m JOIN m.director d WHERE m.id = :movieId")
    Director findDirectorByMovieId(@Param("movieId") Integer movieId);
}
