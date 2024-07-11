package com.hiago.planner.controller;

import com.hiago.planner.dto.activity.ActivityData;
import com.hiago.planner.dto.activity.ActivityRequestPayload;
import com.hiago.planner.dto.activity.ActivityResponse;
import com.hiago.planner.model.Activity;
import com.hiago.planner.model.Trip;
import com.hiago.planner.service.ActivityService;
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
    private TripService service;

    @PostMapping
    public ResponseEntity<TripCreatedResponse> createTrip(@RequestBody TripRequestPayload payload){
    var newTrip= service.crateTrip(payload);
    return ResponseEntity.ok(new TripCreatedResponse(newTrip.getId()));
    }
    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId){
        Trip trip= service.getTripDetails(tripId);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}")
    @Transactional
    public ResponseEntity<Trip> updateTripDetails(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload){
        var trip= service.updateTripDetails(tripId, payload);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId){
        Trip rawTrip= service.confirmTrip(tripId);
        return ResponseEntity.ok(rawTrip);
    }
    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload){
        ParticipantCreateResponse response= service.inviteParticipant(tripId, payload);
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

}
