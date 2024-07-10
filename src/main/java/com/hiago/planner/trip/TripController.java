package com.hiago.planner.trip;

import com.hiago.planner.participant.Participant;
import com.hiago.planner.participant.ParticipantService;
import com.hiago.planner.participant.dto.ParticipantCreateResponse;
import com.hiago.planner.participant.dto.ParticipantData;
import com.hiago.planner.participant.dto.ParticipantRequestPayload;
import com.hiago.planner.trip.dto.TripCreatedResponse;
import com.hiago.planner.trip.dto.TripRequestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private TripRepository tripRepository;

    @PostMapping
    public ResponseEntity<TripCreatedResponse> createTrip(@RequestBody TripRequestPayload payload){
    Trip newTrip= new Trip(payload);

    this.tripRepository.save(newTrip);
    this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip );
    return ResponseEntity.ok(new TripCreatedResponse(newTrip.getId()));
    }
    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId){
        Optional<Trip> trip= this.tripRepository.findById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    @Transactional
    public ResponseEntity<Trip> updateTripDetails(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload){
        Optional<Trip> trip= this.tripRepository.findById(tripId);
        if(trip.isPresent()){
            Trip rowTrip= trip.get();
            rowTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rowTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rowTrip.setDestination(payload.destination());
            return ResponseEntity.ok(rowTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId){
        Optional<Trip> trip= this.tripRepository.findById(tripId);

        if(trip.isPresent()){
            Trip rowTrip= trip.get();
            rowTrip.setIsConfirmed(true);
            this.participantService.triggerConfirmationEmailToParticipants(tripId);
            this.tripRepository.save(rowTrip);
            return ResponseEntity.ok(rowTrip);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload){
        Optional<Trip> trip= this.tripRepository.findById(tripId);
        if(trip.isPresent()){
            Trip rawTrip= trip.get();

           ParticipantCreateResponse participantResponse= this.participantService.registerParticipantToEvent(payload.email(),rawTrip);
            if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();

    }
    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID tripId){
        List<ParticipantData> participantsList= this.participantService.getAllParticipantsFromTrip(tripId);
        return ResponseEntity.ok(participantsList);
    }
}
