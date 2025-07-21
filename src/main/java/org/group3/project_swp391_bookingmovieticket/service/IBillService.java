package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.dto.BookingRequestDTO;
import org.group3.project_swp391_bookingmovieticket.entity.Bill;

import java.util.Optional;


public interface IBillService extends IGeneralService<Bill>{
    Bill createNewBill(BookingRequestDTO bookingRequestDTO);

    Optional<Bill> findById(Integer id);
}
