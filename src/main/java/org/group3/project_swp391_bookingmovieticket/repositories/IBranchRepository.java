package org.group3.project_swp391_bookingmovieticket.repositories;

import org.group3.project_swp391_bookingmovieticket.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBranchRepository extends JpaRepository<Branch, Integer> {

    @Query("SELECT DISTINCT b.location FROM Branch b")
    List<String> findAllLocationBranch();

    List<Branch> findByLocation(String location);
}
