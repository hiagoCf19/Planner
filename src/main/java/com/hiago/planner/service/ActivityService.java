package com.hiago.planner.service;
import com.hiago.planner.dto.activity.ActivityData;
import com.hiago.planner.dto.activity.ActivityWithDate;
import com.hiago.planner.exception.IncompatibleTimeException;
import com.hiago.planner.model.Activity;
import com.hiago.planner.dto.activity.ActivityRequestPayload;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<ActivityWithDate> getAllActivitiesFromId(UUID tripId) {
        var trips= this.repository.findByTripId(tripId);

        List<ActivityData> allActivities = trips.stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();

        Map<LocalDate, List<ActivityData>> activitiesByDate = allActivities.stream()
//                Agrupar baseado na função:
                .collect(Collectors.groupingBy(activity -> activity.occurs_at().toLocalDate()));

        return activitiesByDate.entrySet().stream()
                .map(entry -> new ActivityWithDate(entry.getKey(), entry.getValue()))
                .toList();


    }
    private void checkActivityTime(Trip trip, ActivityRequestPayload payload){
        var occursAt= LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);
        if(occursAt.isBefore(trip.getStartsAt()) || occursAt.isAfter(trip.getEndsAt())){
            throw new IncompatibleTimeException("The activity must be scheduled during travel time");
        }

    }
    private List<ActivityData> activityToDate(List<ActivityData> activities, LocalDateTime date){
        var acs= activities.stream()
                .filter(a -> a.occurs_at().toLocalDate().isEqual(date.toLocalDate()))
                .distinct() // Remover duplicatas baseadas no equals e hashCode de ActivityData
                .toList();
        return acs.stream().map(a -> new ActivityData(a.activityId(), a.title(), a.occurs_at())).toList();

    }
}
