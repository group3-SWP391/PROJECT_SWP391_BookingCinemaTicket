package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.repositories.IPopcornDrinkRepository;
import org.group3.project_swp391_bookingmovieticket.services.IPopcornDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PopcornDrinkService implements IPopcornDrinkService {
    @Autowired
    private IPopcornDrinkRepository iPopcornDrinkRepository;
    @Override
    public List<PopcornDrink> findAll() {
        return iPopcornDrinkRepository.findAll();
    }

    @Override
    public Optional<PopcornDrink> findById(Integer id) {
        return iPopcornDrinkRepository.findById(id);
    }

    @Override
    public void update(PopcornDrink popcornDrink) {
        iPopcornDrinkRepository.save(popcornDrink);

    }

    @Override
    public void remove(Integer id) {

    }
}
