package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.DirectorDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Director;
import org.group3.project_swp391_bookingmovieticket.repository.IDirectorRepository;
import org.group3.project_swp391_bookingmovieticket.service.IDirectorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService implements IDirectorService {
    @Autowired
    private IDirectorRepository directorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DirectorDTO findDirectorByMovieId(int movieId) {
        Director director =  directorRepository.findDirectorByMovieId(movieId);
        return modelMapper.map(director, DirectorDTO.class);
    }

    @Override
    public List<DirectorDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<DirectorDTO> findById(Integer id) {
        return directorRepository.findById(id)
                .map(director -> modelMapper.map(director, DirectorDTO.class));
    }

    @Override
    public void update(DirectorDTO directorDTO) {

    }

    @Override
    public void remove(Integer id) {

    }
}
