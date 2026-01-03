package com.smartsla.smart_sla_tracker.repository;

import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findByRole(Role role);
}
