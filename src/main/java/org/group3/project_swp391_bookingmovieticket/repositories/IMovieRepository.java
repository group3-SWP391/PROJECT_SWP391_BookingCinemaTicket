package org.group3.project_swp391_bookingmovieticket.repositories;


import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByNameContainingIgnoreCase(String movieName);
}
