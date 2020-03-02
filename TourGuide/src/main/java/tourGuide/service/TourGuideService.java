package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gpsUtil.location.Attraction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.entity.RewardAttractionToUser;
import tourGuide.entity.ProposalAttraction;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
@Slf4j
public class TourGuideService {
    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;

    private Boolean inMemoryMode = true;

    @Value("${attractions.proximity.max}")
    private String maxAttractions;

    public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;

        if (inMemoryMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(String userName) {
        return getUser(userName).getUserRewards();
    }

    @Async
    public CompletableFuture<Location> getUserLocation(User user) {

        return CompletableFuture.completedFuture(user)
                .thenApply(userAsync -> Optional.of(userAsync)
                        .map(User::getLastVisitedLocation)
                        .orElse(trackUserLocation(userAsync)))
                .thenApply(visitedLocation -> visitedLocation.location);

    }

    @Async
    public CompletableFuture<Location> getUserLocation(String userName) {

        return CompletableFuture.completedFuture(getUser(userName))
                .thenApply(user -> Optional.of(user)
                        .map(User::getLastVisitedLocation)
                        .orElse(trackUserLocation(user)))
                .thenApply(visitedLocation -> visitedLocation.location);

    }

    public ProposalAttraction getBestPropositionForUser(String userName) {

        try {
            //Get User and his location
            User user = getUser(userName);
            Location location = getUserLocation(user).get();

            //Calculate 5 first locations
            List<RewardAttractionToUser> rewards = getNearByAttractions(location);
            long start = System.currentTimeMillis();
            rewards
                    .forEach(rewardAttractionToUser -> {
                        try {
                            rewardAttractionToUser
                                    .setRewardPoints(rewardsService.getRewardPoints(rewardAttractionToUser.getAttractionId(), user.getUserId()).get());
                        } catch (InterruptedException | ExecutionException e) {
                            logger.warn("Impossible to calculate reward for : " + rewardAttractionToUser.toString(), e);
                        }
                    });
            logger.debug("Calc all reward in : " + (System.currentTimeMillis() - start));
            return ProposalAttraction.builder()
                    .attractionToUsers(rewards)
                    .userLatitude(location.latitude)
                    .userLongitude(location.longitude)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Impossible to get location of the User", e);
        }
        return null;
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(internalUserMap.values());
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public List<Provider> getTripDeals(String userName) {
        User user = getUser(userName);
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(UserReward::getRewardPoints).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public VisitedLocation trackUserLocation(User user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        try {
            rewardsService.calculateRewards(user);
        } catch (ExecutionException | InterruptedException e) {
            logger.warn("Impossible to track user : " + user, e);
        }
        return visitedLocation;
    }


    public List<RewardAttractionToUser> getNearByAttractions(Location visitedLocation) {

        return gpsUtil.getAttractions()
                .parallelStream()
                .map(attraction -> {
                    try {
                        long start = System.currentTimeMillis();
                        RewardAttractionToUser reward = calcRewardAttractionToUser(attraction, visitedLocation).get();
                        logger.debug("Reward calc in : " + (System.currentTimeMillis() - start) + " ms");
                        return reward;

                    } catch (InterruptedException | ExecutionException e) {
                        logger.warn("Impossible to create reward for : " + attraction.toString(), e);
                        return null;
                    }
                }).sorted()
                .limit(Long.parseLong(maxAttractions))
                .collect(Collectors.toList());

    }

    @Async
    public CompletableFuture<RewardAttractionToUser> calcRewardAttractionToUser(Attraction attraction, Location location) {
        return CompletableFuture.completedFuture(RewardAttractionToUser
                .builder()
                .attractionId(attraction.attractionId)
                .attractionLatitude(attraction.latitude)
                .attractionLongitude(attraction.longitude)
                .attractionName(attraction.attractionName)
                .distance(rewardsService.getDistance(attraction, location))
                .build());
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();

    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }


    /**
     * @return All last location of users with userId in key and location in value
     */
    public Map<UUID, Location> getMappingLocationUser() {
        return internalUserMap.values()
                .stream()
                .filter(user -> user.getLastVisitedLocation() != null)
                .collect(Collectors.toMap(User::getUserId, user -> user.getLastVisitedLocation().location));

    }
}
