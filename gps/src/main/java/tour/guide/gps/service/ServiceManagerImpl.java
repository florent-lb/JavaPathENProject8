package tour.guide.gps.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceManagerImpl implements ServiceManager {


    @Autowired
    @Getter
    GpsService gpsService;


}
