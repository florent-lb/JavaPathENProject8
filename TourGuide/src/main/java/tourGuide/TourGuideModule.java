package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rewardCentral.RewardCentral;
import tourGuide.client.ClientGPS;
import tourGuide.service.RewardsService;

@Configuration
public class TourGuideModule {

	@Autowired
	private ClientGPS clientGPS;

	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService( getRewardCentral(),clientGPS);
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
