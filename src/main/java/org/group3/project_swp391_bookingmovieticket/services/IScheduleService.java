package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Schedule;

import java.util.List;
import java.util.Optional;

public interface IScheduleService {
    List<Schedule> findAll();
    Optional<Schedule> findById(Integer id);
    void save(Schedule entity);
    void update(Schedule entity);
    void remove(Integer id);
    void delete(Integer id);
    List<ScheduleDTO> findByBranchId(Integer branchId);
}