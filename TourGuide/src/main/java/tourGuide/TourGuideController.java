package tourGuide;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import reactor.core.publisher.Mono;
import tourGuide.entity.Location;
import tourGuide.entity.ProposalAttraction;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;


	
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @GetMapping(value = "/getLocation",produces = APPLICATION_JSON_VALUE)
    public Mono<Location> getLocation(@RequestParam String userName) {
		return tourGuideService.getUserLocation(userName);
    }

    @GetMapping(value = "/getNearbyAttractions",produces = APPLICATION_JSON_VALUE)
    public ProposalAttraction getNearbyAttractions(@RequestParam String userName) {
        logger.info("Request GET : /getNearbyAttractions : " + userName );

    	return tourGuideService.getBestPropositionForUser(userName);
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(userName));
    }
    
    @GetMapping(value = "/getAllCurrentLocations",produces = APPLICATION_JSON_VALUE)
    public Map<UUID, Location> getAllCurrentLocations() {
        return tourGuideService.getMappingLocationUser();
    }
    
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(userName);
    	return JsonStream.serialize(providers);
    }


}