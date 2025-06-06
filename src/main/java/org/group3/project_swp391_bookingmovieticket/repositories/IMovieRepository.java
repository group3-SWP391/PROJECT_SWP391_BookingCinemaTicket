package org.group3.project_swp391_bookingmovieticket.repositories;


import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByNameContainingIgnoreCase(String movieName);

    @Query("SELECT DISTINCT m.categories FROM Movie m")
    List<String>getMovieCategories();

    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= CURRENT_DATE  AND m.endDate >= CURRENT_DATE ")
    List<Movie> findMovieComingSoon();

    @Query("SELECT m FROM Movie m WHERE m.categories LIKE %:categoryName%")
    List<Movie> getMovieByCategory(@Param("categoryName") String categoryName);

    Movie findMovieById(Integer id);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= CURRENT_DATE  AND m.endDate >= CURRENT_DATE ")
    List<Movie> findMovieNowShowing();

    @Query(value = "SELECT TOP 8 * FROM movie m " +
            "WHERE YEAR(m.release_Date) = YEAR(GETDATE()) " +
            "AND MONTH(m.release_Date) = MONTH(GETDATE()) " +
            "ORDER BY m.views DESC", nativeQuery = true)
    List<Movie> findMovieByViewDesc();


    @Query(value = "SELECT m FROM Movie m WHERE m.categories LIKE %:categoryName%")
    List<Movie> findMovieSameCategory(@Param("categoryName") String categoryName);
}
