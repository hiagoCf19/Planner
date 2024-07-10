package com.hiago.planner.controller;

import com.hiago.planner.model.Participant;
import com.hiago.planner.dto.participant.ParticipantRequestPayload;
import com.hiago.planner.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("{participantId}/confirm")
    @Transactional
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantId, @RequestBody ParticipantRequestPayload payload){
        Optional<Participant> participant= this.participantRepository.findById(participantId);

        if(participant.isPresent()){
            Participant rawParticipant= participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
