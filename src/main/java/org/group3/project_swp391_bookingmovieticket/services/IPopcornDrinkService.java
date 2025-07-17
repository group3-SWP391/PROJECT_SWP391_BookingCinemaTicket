package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.PopcornDrink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPopcornDrinkService extends IGeneralService<PopcornDrink> {
    void save (PopcornDrink popcornDrink);
    Page<PopcornDrink> findAllPagination(Pageable pageable);
}
