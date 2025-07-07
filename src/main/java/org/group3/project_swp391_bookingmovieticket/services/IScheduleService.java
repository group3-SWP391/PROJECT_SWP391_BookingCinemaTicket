package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface IScheduleService extends IGeneralService{
    List<Schedule> findSchedulesByBranchMovieAndDateRange(Integer branchId, String  movieId, LocalDate today, LocalDate threeDay);

}
