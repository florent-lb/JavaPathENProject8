package tourGuide.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class RewardAttractionToUser implements Comparable<RewardAttractionToUser> {

    private final double attractionLongitude;

    private final double attractionLatitude;

    private final String attractionName;

    @JsonIgnore
    private final UUID attractionId;

    private Double distance;

    private int rewardPoints;

    @Override
    public int compareTo(@NotNull RewardAttractionToUser distance) {
        return Double.compare(this.distance, distance.distance);
    }
}
