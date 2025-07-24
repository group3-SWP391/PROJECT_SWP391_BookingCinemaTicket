package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMovieActorRepository extends JpaRepository<MovieActor, Integer> {

    @Query("SELECT ma FROM MovieActor ma WHERE ma.movie.id = :movieId ")
    List<MovieActor> findAllActorByMovieId(@Param("movieId") Integer movieId);

    List<MovieActor> findByMovie_Id(Integer movieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MovieActor ma WHERE ma.movie.id = :movieId")
    void deleteByMovie_Id(@Param("movieId") Integer movieId);

}
