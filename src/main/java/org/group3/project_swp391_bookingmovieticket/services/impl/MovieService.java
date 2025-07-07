package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.repositories.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.services.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MovieService implements IMovieService {
    @Autowired
    private IMovieRepository iMovieRepository;

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        Optional<Movie> movie = iMovieRepository.findById(id);
        return movie;
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public Optional<Movie> getMovieByName(String movieName) {
        return iMovieRepository.findByNameIgnoreCase(movieName);
    }
}
