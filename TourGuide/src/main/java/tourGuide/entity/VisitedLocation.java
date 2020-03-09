package tourGuide.entity;


import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor
public class VisitedLocation implements Serializable {
    public UUID userId;
    public Location location;
    public Date timeVisited;

}
