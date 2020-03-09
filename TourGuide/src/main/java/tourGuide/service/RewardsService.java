package tourGuide.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import rewardCentral.RewardCentral;
import tourGuide.client.ClientGPS;
import tourGuide.entity.Attraction;
import tourGuide.entity.Location;
import tourGuide.entity.VisitedLocation;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
@Slf4j
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final RewardCentral rewardsCentral;

    private ClientGPS clientGPS;

    public RewardsService(RewardCentral rewardCentral, ClientGPS clientGPS) {

        this.rewardsCentral = rewardCentral;
        this.clientGPS = clientGPS;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    private AtomicInteger compteur = new AtomicInteger(0);

    @Async
    public void calculateRewards(User user) {

        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = clientGPS.getAttractions();

        userLocations.forEach(visitedLocation -> {
            attractions.forEach(attraction ->
            {
                if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        try {
                            user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user).get()));
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("Error on calc reward for user " + user.toString(), e);
                        }
                    }
                }
            });
        });


    }


    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    @Async
    public CompletableFuture<Integer> getRewardPoints(UUID attractionId, UUID userId) {
        return CompletableFuture.completedFuture(rewardsCentral.getAttractionRewardPoints(attractionId, userId));
    }

    @Async
    public CompletableFuture<Integer> getRewardPoints(Attraction attraction, User user) {
        return getRewardPoints(attraction.attractionId, user.getUserId());
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
