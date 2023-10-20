package com.BaseSpringBootProject.repositories;

import com.BaseSpringBootProject.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Override
    Optional<Role> findById(Long aLong);
}
