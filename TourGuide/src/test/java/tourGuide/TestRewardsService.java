package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.mock.mockito.MockBean;
import rewardCentral.RewardCentral;
import tourGuide.client.ClientGPS;
import tourGuide.entity.Attraction;
import tourGuide.entity.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

public class TestRewardsService {
/*
	@MockBean
	private ClientGPS clientGPS;

	@Test
	@Disabled
	//TODO update to Web service
	public void userGetRewards() {
		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = null;//TODO gpsUtil.getAttractions().get(0);
		user.getVisitedLocations().add(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	@Disabled //TODO update to Web service
	public void isWithinAttractionProximity() {
		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		Attraction attraction = null;//gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	@Disabled //TODO update to Web service
	public void nearAllAttractions() {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
//		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		//TODO rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0).getUserName());
		tourGuideService.tracker.stopTracking();

	//	assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	*/
}
