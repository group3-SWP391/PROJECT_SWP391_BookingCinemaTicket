package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Role;
import org.group3.project_swp391_bookingmovieticket.service.IRoleService;

import java.util.List;
import java.util.Optional;

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
}
