package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.PopcornDrink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPopcornDrinkRepository extends JpaRepository<PopcornDrink, Integer> {
    @Query("SELECT p FROM PopcornDrink p")
    Page<PopcornDrink> findAllPagination(Pageable pageable);
}
