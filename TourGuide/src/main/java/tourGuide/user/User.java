package tourGuide.user;

import java.util.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tourGuide.entity.VisitedLocation;
import tripPricer.Provider;

@Data
@EqualsAndHashCode(of = "userId")
public class User implements Comparable<User> {

    private final UUID userId;
    private final String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    private List<UserReward> userRewards = new ArrayList<>();
    private UserPreferences userPreferences = new UserPreferences();
    private List<Provider> tripDeals = new ArrayList<>();

    public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UUID getUserId() {
        return userId;
    }



    public void addUserReward(UserReward userReward) {
        if (userRewards.stream().noneMatch(reward -> !reward.attraction.attractionName.equals(userReward.attraction))) {
            userRewards.add(userReward);
        }
    }

    public VisitedLocation getLastVisitedLocation() {
    	if(!visitedLocations.isEmpty())
		{
			return visitedLocations.get(visitedLocations.size() - 1);
		}
    	else
		{
			return null;
		}
    }

    @Override
    public int compareTo(User o) {
        return this.getUserName().compareTo(o.userName);
    }
}
