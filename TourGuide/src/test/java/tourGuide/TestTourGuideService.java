package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.mock.mockito.MockBean;
import rewardCentral.RewardCentral;
import tourGuide.client.ClientGPS;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

public class TestTourGuideService {
/*
	@MockBean
	public ClientGPS clientGPS;

	@Test
	@Disabled //TODO to update with new service
	public void getUserLocation() throws ExecutionException, InterruptedException {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	//	VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get();
		tourGuideService.tracker.stopTracking();
		//assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
	
	@Test
	@Ignore
	public void addUser() {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		//User retrivedUser = tourGuideService.getUser(user.getUserName());
		//User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		//assertEquals(user, retrivedUser);
		//assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	@Disabled //TODO to update with new service
	public void trackUser() throws ExecutionException, InterruptedException {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		//VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
	//	assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
	//@Ignore // Not yet implemented
	@Test
	@Disabled //TODO to update with new service
	public void getNearbyAttractions() {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.trackUserLocation(user);
		
		//TODO List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		
		tourGuideService.tracker.stopTracking();
		
		//TODO assertEquals(5, attractions.size());
	}
	
	public void getTripDeals() {

		RewardsService rewardsService = new RewardsService( new RewardCentral(),clientGPS);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService( rewardsService,clientGPS);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user.getUserName());
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(10, providers.size());
	}
	
	*/
}
