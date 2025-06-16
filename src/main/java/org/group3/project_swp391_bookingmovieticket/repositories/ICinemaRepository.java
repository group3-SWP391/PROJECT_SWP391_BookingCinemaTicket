package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICinemaRepository extends JpaRepository<Cinema, Integer> {
    
    // Find all active cinemas
    List<Cinema> findByIsActive(Integer isActive);
    
    // Find cinema by name
    Optional<Cinema> findByName(String name);
    
    // Find cinema by name and active status
    Optional<Cinema> findByNameAndIsActive(String name, Integer isActive);
    
    // Find cinemas by address containing keyword
    List<Cinema> findByAddressContainingIgnoreCase(String address);
    
    // Find cinemas by phone
    Optional<Cinema> findByPhone(String phone);
    
    // Find cinemas by email
    Optional<Cinema> findByEmail(String email);
    
    // Custom query to find cinemas with room count
    @Query("SELECT c FROM Cinema c LEFT JOIN FETCH c.rooms WHERE c.isActive = :isActive")
    List<Cinema> findActiveCinemasWithRooms(@Param("isActive") Integer isActive);
    
    // Count total rooms in a cinema
    @Query("SELECT COUNT(r) FROM Room r WHERE r.cinema.id = :cinemaId AND r.isActive = 1")
    Long countActiveRoomsByCinemaId(@Param("cinemaId") Integer cinemaId);
    
    // Find cinemas by city/region (assuming address contains city)
    @Query("SELECT c FROM Cinema c WHERE c.address LIKE %:city% AND c.isActive = 1")
    List<Cinema> findByCityAndActive(@Param("city") String city);
} 