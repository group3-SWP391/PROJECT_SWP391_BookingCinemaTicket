package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IScheduleService {
    List<Schedule> getAllSchedules();
    
    Optional<Schedule> getScheduleById(Integer id);
    
    Schedule saveSchedule(Schedule schedule);
    
    Schedule updateSchedule(Schedule schedule);
    
    void deleteSchedule(Integer id);
    
    boolean existsById(Integer id);
    
    List<Schedule> getSchedulesByBranchId(Integer branchId);
    
    List<Schedule> getSchedulesByMovieId(Integer movieId);
    
    List<Schedule> getSchedulesByRoomId(Integer roomId);
    
    List<Schedule> getSchedulesByDate(LocalDate date);
    
    List<Schedule> getSchedulesByBranchAndDate(Integer branchId, LocalDate date);
    
    boolean hasTimeConflict(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer excludeScheduleId);
} 