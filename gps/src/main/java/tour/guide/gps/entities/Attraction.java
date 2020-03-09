package tour.guide.gps.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Attraction( String city,String attractionName, String state,double longitude, double latitude) {
        super(longitude, latitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
    }
}
