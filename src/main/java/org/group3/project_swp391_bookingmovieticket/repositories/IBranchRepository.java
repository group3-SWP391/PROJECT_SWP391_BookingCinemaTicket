package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBranchRepository extends JpaRepository<Branch, Integer> {

    Optional<Branch> findByName(String name);

    List<Branch> findByLocationContainingIgnoreCase(String location);

    Optional<Branch> findByPhoneNo(String phone);

    // Custom query to find Branchs with room count
    @Query("SELECT c FROM Branch c LEFT JOIN FETCH c.rooms")
    List<Branch> findBranchsWithRooms();

    // Count total rooms in a Branch
    @Query("SELECT COUNT(r) FROM Room r WHERE r.branch.id = :cinemaId AND r.isActive = 1")
    Long countActiveRoomsByBranchId(@Param("branchId") Integer branchId);

    // Find Branchs by city/region (assuming address contains city)
    @Query("SELECT c FROM Branch c WHERE c.location LIKE %:city%")
    List<Branch> findByCity(@Param("city") String city);
} 