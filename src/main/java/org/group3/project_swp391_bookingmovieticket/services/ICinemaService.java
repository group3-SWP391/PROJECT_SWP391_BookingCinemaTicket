package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Cinema;

import java.util.List;
import java.util.Optional;

public interface ICinemaService {
    
    // Basic CRUD operations
    List<Cinema> getAllCinemas();
    Optional<Cinema> getCinemaById(Integer id);
    Cinema saveCinema(Cinema cinema);
    Cinema updateCinema(Cinema cinema);
    void deleteCinema(Integer id);
    
    // Business logic methods
    List<Cinema> getActiveCinemas();
    List<Cinema> getCinemasByCity(String city);
    Optional<Cinema> getCinemaByName(String name);
    Optional<Cinema> getCinemaByPhone(String phone);
    Optional<Cinema> getCinemaByEmail(String email);
    
    // Cinema with rooms
    List<Cinema> getCinemasWithRooms();
    Long getRoomCount(Integer cinemaId);
    Long getTotalCapacity(Integer cinemaId);
    
    // Search and filter
    List<Cinema> searchCinemasByAddress(String address);
    boolean isCinemaNameUnique(String name);
    boolean isCinemaPhoneUnique(String phone);
    boolean isCinemaEmailUnique(String email);
    
    // Activate/Deactivate
    void activateCinema(Integer id);
    void deactivateCinema(Integer id);
    
    // Validation
    boolean existsById(Integer id);
    boolean isActiveCinema(Integer id);
} 