package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.dtos.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;
import org.group3.project_swp391_bookingmovieticket.entities.Movie;
import org.group3.project_swp391_bookingmovieticket.repositories.IMovieRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repositories.IBranchRepository;
import org.group3.project_swp391_bookingmovieticket.services.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            dto.setRoomId(schedule.getRoomId() != null ? schedule.getRoomId() : null);
            dto.setMovieId(schedule.getMovie() != null ? schedule.getMovie().getId() : null);

            if (schedule.getMovie() != null) {
                Movie movie = schedule.getMovie();
                dto.setMovieName(movie.getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}