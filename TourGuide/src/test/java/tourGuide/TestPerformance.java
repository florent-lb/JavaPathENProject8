package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rewardCentral.RewardCentral;
import tourGuide.client.ClientConfiguration;
import tourGuide.client.ClientGPS;
import tourGuide.configuration.Application;
import tourGuide.configuration.AsyncConfiguration;
import tourGuide.configuration.PropertiesService;
import tourGuide.entity.Attraction;
import tourGuide.entity.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tripPricer.TripPricer;

@Slf4j(topic = "test.performance")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TourGuideService.class, RewardsService.class, TripPricer.class,
        Tracker.class, RewardCentral.class, AsyncConfiguration.class,
        ClientGPS.class,  PropertiesService.class, ClientConfiguration.class}
)
@PropertySource("application.properties")
@DirtiesContext
public class TestPerformance {

 /*   @Autowired
    private TourGuideService tourGuideService;
*/
    @Autowired
    private ClientGPS clientGPS;
    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */
    @Test
    public void highVolumeTrackLocation() throws InterruptedException {
        RewardsService rewardsService = new RewardsService(new RewardCentral(), clientGPS);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1000);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, clientGPS);

        List<User> allUsers = tourGuideService.getAllUsers();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AtomicInteger compteur = new AtomicInteger(0);

        ConcurrentHashMap<User, VisitedLocation> locationsBack = new ConcurrentHashMap<>();
        for (User user : allUsers) {

            tourGuideService.trackUserLocation(user)
                    .subscribe(visitedLocation -> {
                        compteur.incrementAndGet();
                        locationsBack.put(user, visitedLocation);

                    });
        }
        do {
            logger.info("NB locations" + compteur.get());
            Thread.sleep(100);

        } while (compteur.get() != allUsers.size());


        stopWatch.stop();
        tourGuideService.tracker.stopTracking();
        StringBuilder sbLog = new StringBuilder();
        locationsBack
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach((entry) ->
                sbLog.append("User : ").append(entry.getKey().getUserName()).append("\t|\t").append(" Track to : ").append(entry.getValue().location.toString()).append("\n"));
        logger.trace(sbLog.toString());

        logger.info("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


    @Test
    @Ignore //TODO Update to on Web service GPS
    public void highVolumeGetRewards() {
        RewardsService rewardsService = new RewardsService(new RewardCentral(), clientGPS);

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(10);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(rewardsService, clientGPS);
        AtomicInteger compteur = new AtomicInteger(0);
        Attraction attraction = clientGPS.getAttractions().get(0);
        List<User> allUsers = tourGuideService.getAllUsers();
        AtomicInteger errorCount = new AtomicInteger(0);
        allUsers.forEach(user -> {
            user.getVisitedLocations().add(new VisitedLocation(user.getUserId(), attraction, new Date()));
            rewardsService.calculateRewards(user);
            logger.info("User finish : " + compteur.incrementAndGet());
        });

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        logger.info("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
