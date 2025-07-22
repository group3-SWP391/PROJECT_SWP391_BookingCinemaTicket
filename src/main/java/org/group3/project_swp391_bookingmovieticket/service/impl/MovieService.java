package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.service.IMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return movieRepository.findById(id)
                .map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public void update(MovieDTO movieDTO) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public Page<MovieDTO> findByMovieName(String movieName, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByNameContainingIgnoreCase(movieName, pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public List<String> getMovieCategories() {
        return movieRepository.getMovieCategories();
    }

    @Override
    public Page<MovieDTO> getMovieByCategory(String categoryName, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.getMovieByCategory(categoryName, pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public MovieDTO findMovieById(Integer id) {
        Movie movie = movieRepository.findMovieById(id);
        if (movie == null) return null;
        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public Page<MovieDTO> findMovieNowShowing(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findMovieNowShowing(pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public Page<MovieDTO> findMovieComingSoon(Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findMovieComingSoon(pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public List<MovieDTO> findMovieByViewDesc() {
        return movieRepository.findMovieByViewDesc()
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MovieDTO> findMovieByRatingId(Integer id, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByRating_Id(id, pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public Page<MovieDTO> findByCategory(String categoryName, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByCategories(categoryName, pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public Page<MovieDTO> findMovieByDirector(int directorId, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findByDirectorId(directorId, pageable);
        return moviePage.map(movie -> modelMapper.map(movie, MovieDTO.class));
    }

    @Override
    public Optional<Movie> findMovieEntityById(Integer id) {
        return movieRepository.findById(id);
    }

    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }
}
