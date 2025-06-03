package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieActorRepository extends JpaRepository<MovieActor, Integer> {

    @Query("SELECT ma FROM MovieActor ma WHERE ma.movie.id = :movieId")
    List<MovieActor> findAllActorByMovieId(@Param("movieId") Integer movieId);
}
