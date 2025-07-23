package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IBillService extends IGeneralService<Bill> {
    Bill save(Bill bill);
    List<Bill> findAllByUserIdAndCreatedTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);
    Map<Bill, Integer> countBill(List<Bill> billList);
}
