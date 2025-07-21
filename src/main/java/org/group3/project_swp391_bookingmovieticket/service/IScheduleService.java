package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IScheduleService extends IGeneralService<Schedule>{
    List<Schedule> findSchedulesByBranchMovieAndDateRange(Integer branchId, String  movieId, LocalDate today, LocalDate threeDay);
    HashMap<Schedule, Integer> getTicketCountBySchedule(List<Schedule> scheduleList);
    List<Schedule> findSchedulesByBranchAndDay(Integer branchId, LocalDate today);
}
