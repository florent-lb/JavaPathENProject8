package tourGuide.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(of = "attractionId",callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class Attraction extends Location implements Serializable
{
    public  String attractionName;
    public  String city;
    public  String state;
    public  UUID attractionId;

    public Attraction(double longitude, double latitude) {
        super(longitude, latitude);
    }
}
