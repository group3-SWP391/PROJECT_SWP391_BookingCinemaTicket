package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;

import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<LocalDate> getAllStartDateScheduleByMovieId(int movieId) {
        LocalDate date= LocalDate.now();
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
        return List.of();
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

}
