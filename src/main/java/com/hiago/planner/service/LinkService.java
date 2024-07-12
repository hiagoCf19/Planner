package com.hiago.planner.service;

import com.hiago.planner.dto.links.LinkData;
import com.hiago.planner.dto.links.LinkRequestPayload;
import com.hiago.planner.model.Link;
import com.hiago.planner.model.Trip;
import com.hiago.planner.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository repository;
    private final TripService tripService;

    public Link registerLink(UUID tripId, LinkRequestPayload payload) {
        Trip trip = tripService.getTripDetails(tripId);
        Link newLink = new Link(payload.title(), payload.url(), trip);
        this.repository.save(newLink);
        return newLink;
    }
    public List<LinkData> getAllLinksFromTrip(UUID tripId) {
        return this.repository.findByTripId(tripId).stream().map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
