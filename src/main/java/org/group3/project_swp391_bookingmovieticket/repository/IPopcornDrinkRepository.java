package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPopcornDrinkRepository extends JpaRepository<PopcornDrink, Integer> {
    @Query("SELECT p FROM PopcornDrink p where p.quantity > 0")
    Page<PopcornDrink> findAllPagination(Pageable pageable);
}
