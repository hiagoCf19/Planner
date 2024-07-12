package com.hiago.planner.service;

import com.hiago.planner.dto.activity.ActivityData;
import com.hiago.planner.exception.IncompatibleTimeException;
import com.hiago.planner.model.Activity;
import com.hiago.planner.dto.activity.ActivityRequestPayload;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final TripService tripService;
    private final ActivityRepository repository;

    public Activity registerActivity(UUID tripId, ActivityRequestPayload payload) {

        Trip rawTrip = tripService.getTripDetails(tripId);
        checkActivityTime(rawTrip, payload);
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), rawTrip);
        this.repository.save(newActivity);
        return newActivity;
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
    private void checkActivityTime(Trip trip, ActivityRequestPayload payload){
        var occursAt= LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);
        if(occursAt.isBefore(trip.getStartsAt()) || occursAt.isAfter(trip.getEndsAt())){
            throw new IncompatibleTimeException("The activity must be scheduled during travel time");
        }

    }
}
