package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPopcornDrinkService extends IGeneralService<PopcornDrink> {
    void save (PopcornDrink popcornDrink);
    Page<PopcornDrink> findAllPagination(Pageable pageable);
}
