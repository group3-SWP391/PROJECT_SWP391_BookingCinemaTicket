package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.AdvertisingContactRequest;
import org.group3.project_swp391_bookingmovieticket.repository.IAdvertisingContactRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertisingContactRequestService {

    @Autowired
    private IAdvertisingContactRequestRepository advertisingContactRequestRepository;

    public void save(AdvertisingContactRequest ad) {
        advertisingContactRequestRepository.save(ad);
    }

    public List<AdvertisingContactRequest> findAll() {
        return advertisingContactRequestRepository.findAll();
    }

    public Optional<AdvertisingContactRequest> findById(Integer id) {
        return advertisingContactRequestRepository.findById(id.longValue());
    }

    public void update(AdvertisingContactRequest advertisingContactRequest) {
        advertisingContactRequestRepository.save(advertisingContactRequest);
    }
}
