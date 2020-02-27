package tourGuide;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import gpsUtil.location.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.entity.ProposalAttraction;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
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
    
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    @GetMapping(value = "/getNearbyAttractions",produces = {APPLICATION_JSON_VALUE})
    public ProposalAttraction getNearbyAttractions(@RequestParam String userName) {
        logger.info("Request GET : /getNearbyAttractions : " + userName );

    	return tourGuideService.getBestPropositionForUser(userName);
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }
    
    @GetMapping(value = "/getAllCurrentLocations",produces = {APPLICATION_JSON_VALUE})
    public Map<UUID, Location> getAllCurrentLocations() {
        return tourGuideService.getMappingLocationUser();
    }
    
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}