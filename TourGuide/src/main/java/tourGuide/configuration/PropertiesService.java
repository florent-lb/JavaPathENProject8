package tourGuide.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
@Slf4j
public class PropertiesService {
    private Properties properties;

    public PropertiesService() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            logger.info("application.properties loaded");
        } catch (IOException e) {
            logger.error("Impossible to load properties", e);
        }
    }


  /*  @Bean("clientGpsAddress")
    public String getClientGPSAddress() {
        return properties.get("gps.address").toString();
    }*/


}
