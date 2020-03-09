package tour.guide.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "tour.guide")
public class GpsApplication {

	public static void main(String[] args) {
		System.setProperty("reactor.netty.ioWorkerCount", "50");
		SpringApplication.run(GpsApplication.class, args);
	}

}
