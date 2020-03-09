package tourGuide.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tourGuide.configuration.Application;
import tourGuide.entity.Attraction;
import tourGuide.entity.VisitedLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@Slf4j
//@RibbonClient(name = "tour-gps")
public class ClientGPS {


    //@Autowired
    //@Qualifier("loadBalancedWebClientBuilder")
    public WebClient.Builder clientBuilder = WebClient.builder();

    public Mono<VisitedLocation> getUserPosition(UUID userID)
    {

        return clientBuilder.build()
                .get()
                .uri("http://localhost:8082/user/location"+"?id="+userID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VisitedLocation.class);
    }

    public List<Attraction> getAttractions()
    {
        Flux<Attraction> location = clientBuilder.build().get()
                .uri("http://localhost:8082/attractions")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Attraction.class);

        return location.collectList().block();
    }

}
