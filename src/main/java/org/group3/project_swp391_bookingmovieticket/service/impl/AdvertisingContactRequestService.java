package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.AdvertisingContactRequest;
import org.group3.project_swp391_bookingmovieticket.repository.IAdvertisingContactRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisingContactRequestService {

    @Autowired
    private IAdvertisingContactRequestRepository advertisingContactRequestRepository;

    public void save(AdvertisingContactRequest ad) {
        advertisingContactRequestRepository.save(ad);
    }
}
