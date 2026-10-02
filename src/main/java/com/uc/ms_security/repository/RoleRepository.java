package com.uc.ms_security.repository;

import com.uc.ms_security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Role> findByName(String name);
}
