package tourGuide.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"userLatitude","userLongitude"})
public class ProposalAttraction
{
    private List<RewardAttractionToUser> attractionToUsers;

    private double userLatitude;

    private double userLongitude;


}
