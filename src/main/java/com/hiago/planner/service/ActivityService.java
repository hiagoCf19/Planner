package com.hiago.planner.service;

import com.hiago.planner.dto.activity.ActivityData;
import com.hiago.planner.model.Activity;
import com.hiago.planner.dto.activity.ActivityRequestPayload;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final TripService tripService;
    private final ActivityRepository repository;

    public Activity registerActivity(UUID tripId, ActivityRequestPayload payload) {
        Trip rawTrip = tripService.getTripDetails(tripId);
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), rawTrip);
        this.repository.save(newActivity);
        return newActivity;
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
