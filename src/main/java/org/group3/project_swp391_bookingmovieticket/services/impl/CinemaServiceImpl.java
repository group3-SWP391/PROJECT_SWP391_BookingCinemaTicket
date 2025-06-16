package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Cinema;
import org.group3.project_swp391_bookingmovieticket.repositories.ICinemaRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IRoomRepository;
import org.group3.project_swp391_bookingmovieticket.services.ICinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CinemaServiceImpl implements ICinemaService {
    
    @Autowired
    private ICinemaRepository cinemaRepository;
    
    @Autowired
    private IRoomRepository roomRepository;
    
    @Override
    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }
    
    @Override
    public Optional<Cinema> getCinemaById(Integer id) {
        return cinemaRepository.findById(id);
    }
    
    @Override
    public Cinema saveCinema(Cinema cinema) {
        if (cinema.getIsActive() == null) {
            cinema.setIsActive(1);
        }
        return cinemaRepository.save(cinema);
    }
    
    @Override
    public Cinema updateCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
    
    @Override
    public void deleteCinema(Integer id) {
        // Soft delete - deactivate instead of hard delete
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isPresent()) {
            Cinema existingCinema = cinema.get();
            existingCinema.setIsActive(0);
            cinemaRepository.save(existingCinema);
        }
    }
    
    @Override
    public List<Cinema> getActiveCinemas() {
        return cinemaRepository.findByIsActive(1);
    }
    
    @Override
    public List<Cinema> getCinemasByCity(String city) {
        return cinemaRepository.findByCityAndActive(city);
    }
    
    @Override
    public Optional<Cinema> getCinemaByName(String name) {
        return cinemaRepository.findByName(name);
    }
    
    @Override
    public Optional<Cinema> getCinemaByPhone(String phone) {
        return cinemaRepository.findByPhone(phone);
    }
    
    @Override
    public Optional<Cinema> getCinemaByEmail(String email) {
        return cinemaRepository.findByEmail(email);
    }
    
    @Override
    public List<Cinema> getCinemasWithRooms() {
        return cinemaRepository.findActiveCinemasWithRooms(1);
    }
    
    @Override
    public Long getRoomCount(Integer cinemaId) {
        return cinemaRepository.countActiveRoomsByCinemaId(cinemaId);
    }
    
    @Override
    public Long getTotalCapacity(Integer cinemaId) {
        Long capacity = roomRepository.getTotalCapacityByCinemaId(cinemaId);
        return capacity != null ? capacity : 0L;
    }
    
    @Override
    public List<Cinema> searchCinemasByAddress(String address) {
        return cinemaRepository.findByAddressContainingIgnoreCase(address);
    }
    
    @Override
    public boolean isCinemaNameUnique(String name) {
        return !cinemaRepository.findByName(name).isPresent();
    }
    
    @Override
    public boolean isCinemaPhoneUnique(String phone) {
        return !cinemaRepository.findByPhone(phone).isPresent();
    }
    
    @Override
    public boolean isCinemaEmailUnique(String email) {
        return !cinemaRepository.findByEmail(email).isPresent();
    }
    
    @Override
    public void activateCinema(Integer id) {
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isPresent()) {
            Cinema existingCinema = cinema.get();
            existingCinema.setIsActive(1);
            cinemaRepository.save(existingCinema);
        }
    }
    
    @Override
    public void deactivateCinema(Integer id) {
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isPresent()) {
            Cinema existingCinema = cinema.get();
            existingCinema.setIsActive(0);
            cinemaRepository.save(existingCinema);
        }
    }
    
    @Override
    public boolean existsById(Integer id) {
        return cinemaRepository.existsById(id);
    }
    
    @Override
    public boolean isActiveCinema(Integer id) {
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        return cinema.isPresent() && cinema.get().getIsActive() == 1;
    }
} 