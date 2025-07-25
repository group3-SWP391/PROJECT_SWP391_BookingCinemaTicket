package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.ContactRequest;
import org.group3.project_swp391_bookingmovieticket.repository.IContactRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactRequestService {
    @Autowired
    private IContactRequestRepository contactRequestRepository;

    public void save(ContactRequest contactRequest) {
        contactRequestRepository.save(contactRequest);
    }
}
