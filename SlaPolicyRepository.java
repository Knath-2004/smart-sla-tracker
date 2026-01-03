package com.smartsla.smart_sla_tracker.repository;

import com.smartsla.smart_sla_tracker.entity.Priority;
import com.smartsla.smart_sla_tracker.entity.SlaPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

    Optional<SlaPolicy> findByPriority(Priority priority);
}
