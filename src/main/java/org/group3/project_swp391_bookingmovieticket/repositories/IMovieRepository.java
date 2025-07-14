package org.group3.project_swp391_bookingmovieticket.repositories;


import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Integer> {
    Page<Movie> findByNameContainingIgnoreCase(String movieName, Pageable pageable);

    @Query("SELECT DISTINCT m.categories FROM Movie m")
    List<String>getMovieCategories();

    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= CURRENT_DATE  AND m.endDate >= CURRENT_DATE ")
    Page<Movie> findMovieComingSoon(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.categories LIKE %:categoryName%")
    Page<Movie> getMovieByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    Movie findMovieById(Integer id);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= CURRENT_DATE  AND m.endDate >= CURRENT_DATE ")
    Page<Movie> findMovieNowShowing(Pageable pageable);

    @Query(value = "SELECT TOP 8 * FROM movie m " +
            "WHERE YEAR(m.release_Date) = YEAR(GETDATE()) " +
            "AND MONTH(m.release_Date) = MONTH(GETDATE()) " +
            "ORDER BY m.views DESC", nativeQuery = true)
    List<Movie> findMovieByViewDesc();


    Page<Movie> findByCategories(String category, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.director.id = :directorId")
    Page<Movie> findByDirectorId(@Param("directorId") int directorId, Pageable pageable);
}
