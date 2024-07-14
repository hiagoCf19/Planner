package com.hiago.planner.service;

import com.hiago.planner.dto.participant.ParticipantConfirmRequestPayload;
import com.hiago.planner.exception.ParticipantNotFoundException;
import com.hiago.planner.model.Participant;
import com.hiago.planner.dto.participant.ParticipantCreateResponse;
import com.hiago.planner.dto.participant.ParticipantData;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String remetente;

    private final ParticipantRepository repository;

    public Participant confirmParticipant(UUID participantId, ParticipantConfirmRequestPayload payload){
        Participant participant= searchParticipant(participantId);
            participant.setIsConfirmed(true);
            participant.setName(payload.name());
           return participant;
    }
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
           List<Participant> participants= participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
           this.repository.saveAll(participants);

    }
    public void triggerConfirmationEmailToParticipants(UUID tripId){

    }
    public void triggerConfirmationEmailToParticipant(String email, Trip trip){

            try{
                SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
                simpleMailMessage.setFrom(remetente);
                simpleMailMessage.setTo(email);
                simpleMailMessage.setSubject("Você está sendo convidado para viajar!");
                simpleMailMessage.setText(template(trip, email));
                javaMailSender.send(simpleMailMessage);

            } catch (Exception e){
                System.out.println("erro ao enviar email " + e.getLocalizedMessage());
            }

    }
    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip){
       Participant newParticipant= new Participant(email, trip);
       this.repository.save(newParticipant);
       return new ParticipantCreateResponse(newParticipant.getId());
    }
    public List<ParticipantData> getAllParticipantsFromTrip(UUID tripId){
    return this.repository.findByTripId(tripId).stream().map(p -> new ParticipantData(p.getId(), p.getName(), p.getEmail(), p.getIsConfirmed())).toList();
    }
    private String template(Trip trip, String email){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("pt", "BR"));
        String destination = trip.getDestination();
        String startDate = trip.getStartsAt().format(formatter);
        String endDate = trip.getEndsAt().format(formatter);

        Participant participant= repository.findByEmail(email).orElseThrow(()-> new RuntimeException("participant not found"));
        String baseUrl = "http://localhost:8080/participants/";
        UUID participantId = participant.getId();
        String endpoint = "/confirm";
        String confirmTripUrl = baseUrl + participantId + endpoint;
        return String.format("""
                Você foi convidado(a) para participar de uma viagem para %s nas datas de %s a %s.
                
                Para confirmar sua presença na viagem, clique no link abaixo:
                
                %s
                
                
                Caso você não saiba do que se trata esse e-mail ou não poderá estar presente, apenas ignore esse e-mail.
                """, destination, startDate, endDate, confirmTripUrl);
    }
    private Participant searchParticipant(UUID id){
       return this.repository.findById(id).orElseThrow(() -> new ParticipantNotFoundException("participant not found"));
    }
}
