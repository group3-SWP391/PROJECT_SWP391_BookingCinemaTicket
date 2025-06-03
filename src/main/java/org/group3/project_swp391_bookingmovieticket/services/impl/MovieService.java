package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Director;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.repositories.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.services.IMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MovieDTO> findAll() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDTO> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(MovieDTO movieDTO) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public List<MovieDTO> findByMovieName(String movieName) {
        return movieRepository.findByNameContainingIgnoreCase(movieName)
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMovieCategories() {
        return movieRepository.getMovieCategories();
    }

    @Override
    public List<MovieDTO> getMovieByCategory(String categoryName) {
        return movieRepository.getMovieByCategory(categoryName)
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO findMovieById(Integer id) {
        Movie movie = movieRepository.findMovieById(id);
        if (movie == null) return null;
        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public List<MovieDTO> findMovieNowShowing() {
        return movieRepository.findMovieNowShowing()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findMovieComingSoon() {
        return movieRepository.findMovieComingSoon()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findMovieByViewDesc() {
        return movieRepository.findMovieByViewDesc()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findMovieSameCategory(String categoryName) {
        return movieRepository.findMovieSameCategory(categoryName)
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }
}
