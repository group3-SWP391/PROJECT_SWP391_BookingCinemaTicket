package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.entity.Ticket;
import org.group3.project_swp391_bookingmovieticket.repository.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.repository.ITicketRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BillService implements IBillService {
    @Autowired
    private ITicketRepository ticketRepository;
    @Autowired
    private IBillRepository billRepository;
    @Override
    public void update(Bill bill) {

    }

    @Autowired
    private IBillRepository iBillRepository;
    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findById(Integer id) {
        return Optional.empty();
    }



    @Override
    public void remove(Integer id) {

    }

    @Override
    public Bill save(Bill bill) {

        return billRepository.save(bill);
    }
    @Override
    public List<Bill> findAllByUserIdAndCreatedTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end) {
        return billRepository.findAllByUserIdAndCreatedTimeBetween(userId, start, end);
    }

    @Override
    public Map<Bill, Integer> countBill(List<Bill> billList) {
        List<Integer> integerList = new ArrayList<>();
        Map<Bill, Integer> billIntegerHashMap = new HashMap<>();
        for(Bill bill: billList){
            List<Ticket> ticketList = ticketRepository.findByBillId(bill.getId());
            billIntegerHashMap.put(bill, ticketList.size());
        }
        return billIntegerHashMap;




    }

}
