package com.fjsimon.uberweisung.repository;

import com.fjsimon.uberweisung.domain.repository.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String name);
}
