package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContactRequestRepository extends JpaRepository<ContactRequest, Long> {
}
