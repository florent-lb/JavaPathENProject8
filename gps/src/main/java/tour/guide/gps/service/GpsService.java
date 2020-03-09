package tour.guide.gps.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tour.guide.gps.entities.Attraction;
import tour.guide.gps.entities.VisitedLocation;

import java.util.UUID;


public interface GpsService
{

    Mono<VisitedLocation> getUserLocation(UUID user);


    Flux<Attraction> getAttractions();

}
