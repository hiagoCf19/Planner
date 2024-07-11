package com.hiago.planner.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.hiago.planner.model.Activity;
import com.hiago.planner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByTripId(UUID tripId);
}
