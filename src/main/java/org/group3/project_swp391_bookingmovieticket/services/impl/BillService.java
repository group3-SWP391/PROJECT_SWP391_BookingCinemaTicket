package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Bill;
import org.group3.project_swp391_bookingmovieticket.repositories.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.services.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BillService implements IBillService {
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
    public void update(Object o) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public Bill save(Bill bill) {
        return iBillRepository.save(bill);
    }
}
