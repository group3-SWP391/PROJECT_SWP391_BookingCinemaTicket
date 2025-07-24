package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.BranchDTO;
import org.group3.project_swp391_bookingmovieticket.dto.MovieDTO;
import org.group3.project_swp391_bookingmovieticket.dto.RoomDTO;
import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.repository.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IBranchRepository branchRepository;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<LocalDate> getAllStartDateScheduleByMovieId(int movieId) {
        LocalDate date = LocalDate.now();
        return scheduleRepository.getAllStartDateScheduleByMovieId(movieId)
                .stream()
                .filter(startDate -> startDate.compareTo(date) >= 0)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO getScheduleByScheduleId(int scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId);
        return modelMapper.map(schedule, ScheduleDTO.class);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Optional<Schedule> findById(Integer id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public void update(Schedule schedule) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public List<ScheduleDTO> findByBranchId(Integer branchId) {
        if (branchId == null) {
            return Collections.emptyList();
        }
        if (!branchRepository.existsById(branchId)) {
            return Collections.emptyList();
        }
        List<Schedule> schedules = scheduleRepository.findByBranchId(branchId);
        return schedules.stream().map(schedule -> {
            ScheduleDTO dto = new ScheduleDTO();
            dto.setId(schedule.getId());
            dto.setBranch(modelMapper.map(schedule.getBranch(), BranchDTO.class));
            dto.setStartDate(schedule.getStartDate());
            dto.setEndTime(schedule.getEndTime());
            dto.setStartTime(schedule.getStartTime());
            dto.setPrice(schedule.getPrice());

            dto.setImgurl(schedule.getBranch().getImgURL());
            dto.setRoom(modelMapper.map(schedule.getRoom(), RoomDTO.class));
            dto.setMovie(modelMapper.map(schedule.getMovie(), MovieDTO.class));

            if (schedule.getMovie() != null) {
                Movie movie = schedule.getMovie();
                dto.getMovie().setName(movie.getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

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
                .filter(schedule -> excludeScheduleId == null || schedule.getId() != excludeScheduleId)
                .anyMatch(existing -> isTimeOverlapping(startTime, endTime, existing.getStartTime(), existing.getEndTime()));
    }

    // Helper method to check if two time ranges overlap
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        // Two time ranges overlap if they don't NOT overlap
        // They don't overlap if one ends before the other starts
        return !(end1.isBefore(start2) || end1.equals(start2) || start1.isAfter(end2) || start1.equals(end2));
    }
}


