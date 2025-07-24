package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPopcornDrinkRepository extends JpaRepository<PopcornDrink, Integer> {
    List<PopcornDrink> findByQuantityGreaterThan(Integer quantity);
}
