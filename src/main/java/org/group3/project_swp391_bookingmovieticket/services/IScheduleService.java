package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface IScheduleService extends IGeneralService<Schedule> {
    List<LocalDate> getAllStartDateScheduleByMovieId(int movieId);
}
