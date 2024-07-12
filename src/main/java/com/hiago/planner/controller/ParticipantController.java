package com.hiago.planner.controller;

import com.hiago.planner.model.Participant;
import com.hiago.planner.dto.participant.ParticipantRequestPayload;
import com.hiago.planner.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService service;

    @PostMapping("{participantId}/confirm")
    @Transactional
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantId, @RequestBody ParticipantRequestPayload payload){
        var participant= service.confirmParticipant(participantId, payload);
        return ResponseEntity.ok(participant);
    }
}
