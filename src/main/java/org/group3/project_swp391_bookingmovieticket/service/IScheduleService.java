package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;

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