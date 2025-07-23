package org.group3.project_swp391_bookingmovieticket.repository;

import org.group3.project_swp391_bookingmovieticket.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findAllByUserIdAndCreatedTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);

}
