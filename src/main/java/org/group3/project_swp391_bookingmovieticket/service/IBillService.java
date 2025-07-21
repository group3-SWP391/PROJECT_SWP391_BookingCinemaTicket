package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;

public interface IBillService extends IGeneralService<Bill> {
    Bill save(Bill bill);
}
