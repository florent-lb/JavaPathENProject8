package tour.guide.gps.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tour.guide.gps.entities.Attraction;
import tour.guide.gps.entities.VisitedLocation;
import tour.guide.gps.service.ServiceManager;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@Slf4j
public class GpsHandler {


    @Autowired
    private ServiceManager manager;

    @GetMapping(value = "/user/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<VisitedLocation> getPosition(@RequestParam("id") String idUser) {
        log.info("GET /user/location/"+idUser);
        return  manager.getGpsService()
                .getUserLocation(UUID.fromString(idUser)).delayElement(Duration.of(50, ChronoUnit.MILLIS));
    }

    @GetMapping(value = "/attractions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Attraction> getAttractions() {
        return manager.getGpsService().getAttractions();
    }

}
