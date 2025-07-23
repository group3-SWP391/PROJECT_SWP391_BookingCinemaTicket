package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.repository.IPopcornDrinkRepository;
import org.group3.project_swp391_bookingmovieticket.service.IPopcornDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class PopcornDrinkService implements IPopcornDrinkService {
    @Autowired
    private IPopcornDrinkRepository popcornDrinkRepository;
    @Override
    public List<PopcornDrink> findAll() {
        List<PopcornDrink> popcornDrink = new ArrayList<>();
       List<PopcornDrink> popcornDrinkList = popcornDrinkRepository.findAll();
       for(PopcornDrink popcorn: popcornDrinkList){
           if(popcorn.getQuantity() > 0){
               popcornDrink.add(popcorn);
           }
       }
       return popcornDrink;
    }

    @Override
    public Optional<PopcornDrink> findById(Integer id) {

        return popcornDrinkRepository.findById(id);
    }

    @Override
    public void update(PopcornDrink popcornDrink) {
        popcornDrinkRepository.save(popcornDrink);

    }

    @Override
    public void remove(Integer id) {

    }
}
