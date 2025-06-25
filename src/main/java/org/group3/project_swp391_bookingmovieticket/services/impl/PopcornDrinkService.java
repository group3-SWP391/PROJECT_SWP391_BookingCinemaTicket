package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.repositories.IPopcornDrinkRepository;
import org.group3.project_swp391_bookingmovieticket.services.IPopcornDrinkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PopcornDrinkService implements IPopcornDrinkService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IPopcornDrinkRepository popcornDrinkRepository;

    @Override
    public List<PopcornDrink> findAll() {
        return popcornDrinkRepository.findAll();
    }

    @Override
    public Optional<PopcornDrink> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(PopcornDrink popcornDrink) {

    }

    @Override
    public void remove(Integer id) {

    }
}
