package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Bill;

public interface IBillService extends IGeneralService<Bill>{
    void createNewBill(BookingRequestDTO bookingRequestDTO);
}
