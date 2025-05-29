package org.group3.project_swp391_bookingmovieticket.services.impl;

import org.group3.project_swp391_bookingmovieticket.entities.Role;
import org.group3.project_swp391_bookingmovieticket.services.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleService implements IRoleService {


    @Override
    public List<Role> findAll() {
        return List.of();
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Role role) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void save(Role role) {

    }
}
