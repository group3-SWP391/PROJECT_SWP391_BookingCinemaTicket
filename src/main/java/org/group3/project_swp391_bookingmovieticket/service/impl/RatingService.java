package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Rating;
import org.group3.project_swp391_bookingmovieticket.repository.IRatingRepository;
import org.group3.project_swp391_bookingmovieticket.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private IRatingRepository ratingRepository;

    @Override
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Rating> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Rating rating) {

    }

    @Override
    public void remove(Integer id) {

    }
}
