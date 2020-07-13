package com.xfst.simpleblog.repository;

import com.xfst.simpleblog.repository.data.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(final String username);
}
