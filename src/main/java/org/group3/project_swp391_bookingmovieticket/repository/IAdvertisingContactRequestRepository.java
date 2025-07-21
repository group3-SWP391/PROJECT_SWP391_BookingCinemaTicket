package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.AdvertisingContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdvertisingContactRequestRepository extends JpaRepository<AdvertisingContactRequest, Integer> {
} 