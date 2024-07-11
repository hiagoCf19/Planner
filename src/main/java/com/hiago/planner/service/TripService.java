package com.hiago.planner.service;

import com.hiago.planner.dto.participant.ParticipantCreateResponse;
import com.hiago.planner.dto.participant.ParticipantRequestPayload;
import com.hiago.planner.dto.trip.TripRequestPayload;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository repository;
    @Autowired
    private ParticipantService participantService;

    public Trip crateTrip(TripRequestPayload payload){
        Trip newTrip= new Trip(payload);
        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip );
        return newTrip;
    }
    public Trip getTripDetails(UUID tripId){
       return searchTrip(tripId);
    }
    public Trip updateTripDetails(UUID tripId, TripRequestPayload payload){
       Trip rawTrip= searchTrip(tripId);
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            return  rawTrip;
    }
    public Trip confirmTrip(UUID tripId){
        Trip rawTrip= searchTrip(tripId);
        rawTrip.setIsConfirmed(true);
        this.participantService.triggerConfirmationEmailToParticipants(tripId);
        this.repository.save(rawTrip);
        return rawTrip;
    }
    public ParticipantCreateResponse inviteParticipant(UUID tripId, ParticipantRequestPayload payload){
        Trip rawTrip= searchTrip(tripId);
        ParticipantCreateResponse participantResponse= this.participantService.registerParticipantToEvent(payload.email(),rawTrip);
        if(rawTrip.getIsConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());
        return participantResponse;
    }
    private Trip searchTrip(UUID id){
        return this.repository.findById(id).orElseThrow(()-> new RuntimeException("Trip not found"));
    }
}