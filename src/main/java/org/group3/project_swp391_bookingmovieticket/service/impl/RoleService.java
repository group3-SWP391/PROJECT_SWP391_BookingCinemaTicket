package org.group3.project_swp391_bookingmovieticket.service.impl;

import org.group3.project_swp391_bookingmovieticket.entity.Role;
import org.group3.project_swp391_bookingmovieticket.repository.IRoleRepository;
import org.group3.project_swp391_bookingmovieticket.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository iRoleRepository;

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
    public Optional<Role> findByName(String name) {
        return iRoleRepository.findByNameIgnoreCase(name);
    }
}
