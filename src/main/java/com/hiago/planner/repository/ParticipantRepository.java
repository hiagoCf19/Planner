package com.hiago.planner.repository;

import com.hiago.planner.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByTripId(UUID tripId);
    Optional<Participant> findByEmail(String email);
}
