package com.xfst.simpleblog.repository;

import com.xfst.simpleblog.repository.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
