package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IBranchRepository extends JpaRepository<Branch, Integer> {

    @Query("SELECT DISTINCT b.location FROM Branch b")
    List<String> findAllLocationBranch();

    List<Branch> findByLocation(String location);
    List<Branch> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT DISTINCT s.branch FROM Schedule s WHERE s.movie.id = :movieId")
    List<Branch> getBranchByMovie(@Param("movieId") Integer movieId);
    Optional<Branch> findByPhoneNo(String phone);

    @Query(value = "SELECT DISTINCT b.* FROM schedule s " +
            "JOIN branch b ON b.id = s.branch_id " +
            "WHERE s.movie_id = :movieId AND CONVERT(varchar, s.start_date, 23) = :startDate", nativeQuery = true)
    List<Branch> getBranchByStartDate(@Param("movieId") Integer movieId, @Param("startDate") String startDate);

    List<Branch> findByName(String name);

    // Custom query to find Branchs with room count
    @Query("SELECT c FROM Branch c LEFT JOIN FETCH c.roomList")
    List<Branch> findBranchsWithRooms();

    // Count total rooms in a Branch
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :cinemaId AND r.isActive = 1")
    Long countActiveRoomsByBranchId(@Param("branchId") Integer branchId);

    // Find Branchs by city/region (assuming address contains city)
    @Query("SELECT c FROM Branch c WHERE c.location LIKE %:city%")
    List<Branch> findByCity(@Param("city") String city);
}
