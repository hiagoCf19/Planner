package com.hiago.planner.service;

import com.hiago.planner.model.Participant;
import com.hiago.planner.dto.participant.ParticipantCreateResponse;
import com.hiago.planner.dto.participant.ParticipantData;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository repository;

public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
       List<Participant> participants= participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
       this.repository.saveAll(participants);
    System.out.println(participants.getFirst().getId());
}
public void triggerConfirmationEmailToParticipants(UUID tripId){

}
    public void triggerConfirmationEmailToParticipant(String email){

    }
    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip){
       Participant newParticipant= new Participant(email, trip);
       this.repository.save(newParticipant);
       return new ParticipantCreateResponse(newParticipant.getId());
    }
    public List<ParticipantData> getAllParticipantsFromTrip(UUID tripId){
    return this.repository.findByTripId(tripId).stream().map(p -> new ParticipantData(p.getId(), p.getName(), p.getEmail(), p.getIsConfirmed())).toList();
    }

}