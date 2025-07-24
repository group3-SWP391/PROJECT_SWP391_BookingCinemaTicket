package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;

import java.util.List;

public interface IPopcornDrinkService extends IGeneralService<PopcornDrink>{
    List<PopcornDrink> getPopcornDrink(Integer quantity);


}
