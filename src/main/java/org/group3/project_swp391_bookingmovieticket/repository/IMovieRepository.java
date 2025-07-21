package org.group3.project_swp391_bookingmovieticket.repository;


import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Integer> {
}
