package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Schedule;
import org.group3.project_swp391_bookingmovieticket.repository.IScheduleRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class ScheduleService implements IScheduleService {
    @Autowired
    private IScheduleRepository iScheduleRepository;
    @Autowired
    private ITicketRepository ticketRepository;
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {
        Optional<Schedule> schedule = iScheduleRepository.findById(id);
        return schedule;


    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }


    @Override
    public List<Schedule> findSchedulesByBranchMovieAndDateRange(Integer branchId, String movieId, LocalDate today, LocalDate threeDay) {
        return iScheduleRepository.findByBranchMovieANDRange(branchId, movieId, today, threeDay);
    }

    @Override
    public HashMap<Schedule, Integer> getTicketCountBySchedule(List<Schedule> scheduleList) {
        HashMap<Schedule, Integer> scheduleTicketCountMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            int count = ticketRepository.countTicketsByScheduleId(schedule.getId());
            scheduleTicketCountMap.put(schedule, count);
        }

        return scheduleTicketCountMap;
    }

    @Override
    public List<Schedule> findSchedulesByBranchAndDay(Integer branchId, LocalDate today) {
        return iScheduleRepository.findByBranchIdAndStartDate(branchId, today);
    }
}
