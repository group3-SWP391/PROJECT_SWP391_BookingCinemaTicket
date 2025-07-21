package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.PopcornDrink;
import org.group3.project_swp391_bookingmovieticket.repository.IPopcornDrinkRepository;
import org.group3.project_swp391_bookingmovieticket.service.IPopcornDrinkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return popcornDrinkRepository.findById(id);
    }

    @Override
    public void update(PopcornDrink popcornDrink) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void save(PopcornDrink popcornDrink) {
        popcornDrinkRepository.save(popcornDrink);
    }

    @Override
    public Page<PopcornDrink> findAllPagination(Pageable pageable) {
        return popcornDrinkRepository.findAllPagination(pageable);
    }
}
