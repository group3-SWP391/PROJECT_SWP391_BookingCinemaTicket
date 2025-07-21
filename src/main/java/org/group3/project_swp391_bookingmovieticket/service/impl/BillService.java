package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.group3.project_swp391_bookingmovieticket.repository.IBillRepository;
import org.group3.project_swp391_bookingmovieticket.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BillService implements IBillService {
    @Autowired
    private IBillRepository iBillRepository;
    @Override
    public boolean existsBillByUserId(Integer userId) {
        return iBillRepository.existsByUserId(userId);

    }

    @Override
    public List<Bill> findAll() {
        return List.of();
    }

    @Override
    public Optional<Bill> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Bill bill) {

    }

    @Override
    public void remove(Integer id) {

    }
}
