package com.clean.architecture.repository;

import com.clean.architecture.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {
    RoleModel findRoleByRoleName(String name);
}
