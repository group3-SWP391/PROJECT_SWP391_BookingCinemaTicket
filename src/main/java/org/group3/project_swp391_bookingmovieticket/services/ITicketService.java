package org.group3.project_swp391_bookingmovieticket.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.group3.project_swp391_bookingmovieticket.entities.Ticket;

import java.util.HashMap;
import java.util.List;

public interface ITicketService extends IGeneralService {
    HashMap<Ticket, Integer> getMovieStatusByTicketCount(String date, String fromHour, String branch);
}
