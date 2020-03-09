package tourGuide.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "tourGuide")
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        System.setProperty("reactor.netty.ioWorkerCount", "100");
        SpringApplication.run(Application.class, args);
    }

}
