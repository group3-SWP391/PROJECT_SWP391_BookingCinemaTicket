package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRatingRepository extends JpaRepository<Rating, Integer> {
    
    // Find all active ratings
    List<Rating> findByIsActive(Integer isActive);
    
    // Find rating by name
    Rating findByName(String name);
} 