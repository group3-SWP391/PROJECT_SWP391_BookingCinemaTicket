package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.MovieActorDTO;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieActorRepository;
import org.group3.project_swp391_bookingmovieticket.service.IMovieActorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieActorService implements IMovieActorService {

    @Autowired
    private IMovieActorRepository movieActorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MovieActorDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<MovieActorDTO> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(MovieActorDTO movieActorDTO) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public List<MovieActorDTO> findAllActorByMovieId(int movieId) {
        return movieActorRepository.findAllActorByMovieId(movieId)
                .stream()
                .map(movieActor -> modelMapper.map(movieActor, MovieActorDTO.class))
                .collect(Collectors.toList());
    }

}
