package com.hiago.planner.controller;

import com.hiago.planner.dto.activity.ActivityData;
import com.hiago.planner.dto.activity.ActivityRequestPayload;
import com.hiago.planner.dto.activity.ActivityResponse;
import com.hiago.planner.dto.links.LinkData;
import com.hiago.planner.dto.links.LinkRequestPayload;
import com.hiago.planner.dto.links.LinkResponse;
import com.hiago.planner.model.Activity;
import com.hiago.planner.model.Link;
import com.hiago.planner.model.Trip;
import com.hiago.planner.service.ActivityService;
import com.hiago.planner.service.LinkService;
import com.hiago.planner.service.ParticipantService;
import com.hiago.planner.dto.participant.ParticipantCreateResponse;
import com.hiago.planner.dto.participant.ParticipantData;
import com.hiago.planner.dto.participant.ParticipantRequestPayload;
import com.hiago.planner.dto.trip.TripCreatedResponse;
import com.hiago.planner.dto.trip.TripRequestPayload;
import com.hiago.planner.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private TripService tripService;
    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripCreatedResponse> createTrip(@RequestBody TripRequestPayload payload){
    var newTrip= tripService.crateTrip(payload);
    return ResponseEntity.ok(new TripCreatedResponse(newTrip.getId()));
    }
    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId){
        Trip trip= tripService.getTripDetails(tripId);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}")
    @Transactional
    public ResponseEntity<Trip> updateTripDetails(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload){
        var trip= tripService.updateTripDetails(tripId, payload);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId){
        Trip rawTrip= tripService.confirmTrip(tripId);
        return ResponseEntity.ok(rawTrip);
    }
    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload){
        ParticipantCreateResponse response= tripService.inviteParticipant(tripId, payload);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID tripId){
        List<ParticipantData> participantsList= this.participantService.getAllParticipantsFromTrip(tripId);
        return ResponseEntity.ok(participantsList);
    }
    @PostMapping("{tripId}/activities")

    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID tripId, @RequestBody ActivityRequestPayload payload){
        Activity activity= this.activityService.registerActivity(tripId, payload);
        return ResponseEntity.ok(new ActivityResponse(activity.getId()));
    }
    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID tripId){
        List<ActivityData> activityDataList= this.activityService.getAllActivitiesFromId(tripId);
        return ResponseEntity.ok(activityDataList);
    }
    @PostMapping("{tripId}/links")

    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequestPayload payload){
        Link link= this.linkService.registerLink(tripId, payload);
        return ResponseEntity.ok(new LinkResponse(link.getId()));
    }
    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID tripId){
        List<LinkData> LinkDataList= this.linkService.getAllLinksFromTrip(tripId);
        return ResponseEntity.ok(LinkDataList);
    }
// TODO:
// ADICIONAR VALIDAÇÃO PARA VERIFICAR SE A DATA DE INCIOI DA VIAGEM É INFERIOR A DATA DE FIM DA VIAGEM.
// VERIFICAR SE A DATA DE UMA ATIVIDADE REGISTRADA ESTA ENTRE AS DATAS DE UMA VIAGEM, A ATIVIDADE DEVESER PROGRAMADA APENAS DURANTE O TEMPO DA VIAGEM
// MAPEAR EXCESSÕES DA APLICAÇÃO, UTILIZAR BEAN VALIDATIONS E ETC.
}


