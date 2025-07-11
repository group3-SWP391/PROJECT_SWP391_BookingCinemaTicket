package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.services.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Optional<Schedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(Integer id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return scheduleRepository.existsById(id);
    }

    @Override
    public List<Schedule> getSchedulesByBranchId(Integer branchId) {
        return scheduleRepository.findByBranchId(branchId);
    }

    @Override
    public List<Schedule> getSchedulesByMovieId(Integer movieId) {
        return scheduleRepository.findByMovieId(movieId);
    }

    @Override
    public List<Schedule> getSchedulesByRoomId(Integer roomId) {
        return scheduleRepository.findByRoomId(roomId);
    }

    @Override
    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByStartDate(date);
    }

    @Override
    public List<Schedule> getSchedulesByBranchAndDate(Integer branchId, LocalDate date) {
        return scheduleRepository.findByBranchIdAndStartDate(branchId, date);
    }

    @Override
    public boolean hasTimeConflict(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer excludeScheduleId) {
        List<Schedule> existingSchedules = scheduleRepository.findSchedulesByRoomAndDate(roomId, date);
        
        return existingSchedules.stream()
                .filter(schedule -> excludeScheduleId == null || !schedule.getId().equals(excludeScheduleId))
                .anyMatch(existing -> isTimeOverlapping(startTime, endTime, existing.getStartTime(), existing.getEndTime()));
    }
    
    // Helper method to check if two time ranges overlap
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        // Two time ranges overlap if they don't NOT overlap
        // They don't overlap if one ends before the other starts
        return !(end1.isBefore(start2) || end1.equals(start2) || start1.isAfter(end2) || start1.equals(end2));
    }
} 