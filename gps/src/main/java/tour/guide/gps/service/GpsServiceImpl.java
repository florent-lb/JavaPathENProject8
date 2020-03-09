package tour.guide.gps.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tour.guide.gps.entities.Attraction;
import tour.guide.gps.entities.VisitedLocation;
import tour.guide.gps.utils.SurchargeGPGUtils;

import java.util.UUID;

@Service
public class GpsServiceImpl implements GpsService {

    @Override
    public Mono<VisitedLocation> getUserLocation(UUID userId) {
        SurchargeGPGUtils gpgUtils = new SurchargeGPGUtils();

        return Mono.fromCallable(() -> gpgUtils.getUserLocation(userId));
    }


    @Override
    public Flux<Attraction> getAttractions() {
        SurchargeGPGUtils gpgUtils = new SurchargeGPGUtils();
        return Flux.fromIterable(gpgUtils.getAttractions());
    }


}
