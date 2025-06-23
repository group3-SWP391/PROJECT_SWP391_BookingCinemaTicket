package org.group3.project_swp391_bookingmovieticket.services;

import org.group3.project_swp391_bookingmovieticket.dtos.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.entities.Bill;

import java.util.Optional;


public interface IBillService extends IGeneralService<Bill>{
    Bill createNewBill(BookingRequestDTO bookingRequestDTO);

    Optional<Bill> findById(Integer id);
}
