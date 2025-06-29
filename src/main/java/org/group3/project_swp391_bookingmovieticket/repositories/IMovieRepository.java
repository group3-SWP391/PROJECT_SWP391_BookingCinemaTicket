package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByNameContainingIgnoreCase(String movieName);

    @Query("SELECT DISTINCT m.categories FROM Movie m")
    List<String> getMovieCategories();

    @Query("SELECT m FROM Movie m WHERE m.categories LIKE %:categoryName%")
    List<Movie> getMovieByCategory(@Param("categoryName") String categoryName);

    List<Movie> findByIsShowing(Integer isShowing);
    Movie findEntityById(Integer id);
    Movie findByName(String name);
    Optional<Movie> findById(Integer id);
}