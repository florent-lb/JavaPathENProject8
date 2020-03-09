package tour.guide.gps.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Data
@EqualsAndHashCode(of = "userId")
public class VisitedLocation implements Serializable {
    public final UUID userId;
    public final Location location;
    public final Date timeVisited;

}
