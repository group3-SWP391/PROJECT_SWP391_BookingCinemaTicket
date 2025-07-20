package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.entity.Movie;
import org.group3.project_swp391_bookingmovieticket.repository.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repository.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void save(Schedule entity) {
        scheduleRepository.save(entity);
    }

    @Override
    public void update(Schedule entity) {
        scheduleRepository.save(entity);
    }

    @Override
    public void remove(Integer id) {
    }

    @Override
    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
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
            dto.setId(schedule.getId() != null ? schedule.getId() : null);
            dto.setBranchId(schedule.getBranch() != null ? schedule.getBranch().getId() : null);
            dto.setStartDate(schedule.getStartDate());
            dto.setEndDate(schedule.getEndDate());
            dto.setStartTime(schedule.getStartTime());
            dto.setPrice(schedule.getPrice());

            dto.setImgurl(schedule.getBranch() != null ? schedule.getBranch().getImgurl() : null);
            dto.setRoom(schedule.getRoom().getId() != null ? schedule.getRoom() : null);
            dto.setMovie(schedule.getMovie() != null ? schedule.getMovie() : null);

            if (schedule.getMovie() != null) {
                Movie movie = schedule.getMovie();
                dto.setMovieName(movie.getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public Schedule getScheduleById(Integer scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule != null) {
            // Force load the movie (if lazy-loaded)
            Hibernate.initialize(schedule.getMovie());

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDateTime = LocalDateTime.of(schedule.getStartDate(), schedule.getStartTime());

            if (now.isAfter(startDateTime)) {
                throw new RuntimeException("Lịch chiếu đã đóng, vui lòng chọn lịch chiếu khác!");
            }

            if (now.toLocalDate().isAfter(schedule.getEndDate())) {
                throw new RuntimeException("Rạp đã ngừng chiếu suất này sau ngày " + schedule.getEndDate());
            }
        }
        return schedule;
    }


}