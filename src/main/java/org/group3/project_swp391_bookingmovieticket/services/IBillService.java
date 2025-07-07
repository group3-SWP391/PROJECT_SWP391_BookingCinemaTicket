package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;

public interface IBillService extends IGeneralService {
    Bill save(Bill bill);
}
