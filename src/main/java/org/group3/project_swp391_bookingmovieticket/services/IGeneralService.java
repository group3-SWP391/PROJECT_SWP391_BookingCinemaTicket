package org.group3.project_swp391_bookingmovieticket.services;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();

    Optional<T> findById(Integer id);

    void update(T t);

    void remove(Integer id);
}

