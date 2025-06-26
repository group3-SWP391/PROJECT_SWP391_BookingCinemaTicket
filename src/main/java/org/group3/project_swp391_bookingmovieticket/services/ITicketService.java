package org.group3.project_swp391_bookingmovieticket.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface ITicketService extends IGeneralService {
    HashMap<Ticket, Integer> getMovieStatusByTicketCount(String date, String fromHour, String branch);
    HashSet<Integer> findBookedSeatIdsBySchedule(int id);
    List<Ticket> findByScheduleId(int id);
}
