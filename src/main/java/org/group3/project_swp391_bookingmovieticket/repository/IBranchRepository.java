package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBranchRepository extends JpaRepository<Branch, Integer> {

    @Query("SELECT DISTINCT b.location FROM Branch b")
    List<String> findAllLocationBranch();

    List<Branch> findByLocation(String location);

    @Query("SELECT DISTINCT s.branch FROM Schedule s WHERE s.movie.id = :movieId")
    List<Branch> getBranchByMovie(@Param("movieId") Integer movieId);

    @Query(value = "SELECT DISTINCT b.* FROM schedule s " +
            "JOIN branch b ON b.id = s.branch_id " +
            "WHERE s.movie_id = :movieId AND CONVERT(varchar, s.start_date, 23) = :startDate", nativeQuery = true)
    List<Branch> getBranchByStartDate(@Param("movieId") Integer movieId, @Param("startDate") String startDate);

    List<Branch> findByName(String name);
}
