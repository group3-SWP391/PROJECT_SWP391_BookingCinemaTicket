package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.ScheduleDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IScheduleService extends IGeneralService<Schedule> {
    List<LocalDate> getAllStartDateScheduleByMovieId(int movieId);
    ScheduleDTO getScheduleByScheduleId(int scheduleId);
    List<Schedule> findAll();
    Optional<Schedule> findById(Integer id);
    List<ScheduleDTO> findByBranchId(Integer branchId);

}