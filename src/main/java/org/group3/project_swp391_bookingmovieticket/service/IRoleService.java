package org.group3.project_swp391_bookingmovieticket.service;

import org.group3.project_swp391_bookingmovieticket.entity.Role;

import java.util.Optional;

public interface IRoleService extends IGeneralService<Role>{
    Optional<Role> findByName(String name);


}
