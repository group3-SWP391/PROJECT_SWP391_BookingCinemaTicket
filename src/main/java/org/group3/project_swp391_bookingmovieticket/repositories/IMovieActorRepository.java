package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.MovieActor;
import org.group3.project_swp391_bookingmovieticket.entities.MovieActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IMovieActorRepository extends JpaRepository<MovieActor, MovieActorId> {
    
    List<MovieActor> findByMovie_Id(Integer movieId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MovieActor ma WHERE ma.movie.id = :movieId")
    void deleteByMovie_Id(@Param("movieId") Integer movieId);
} 