package tourGuide.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

    @Value("${tour.guide.concurrency.executor.core.pool.size}")
    private String coreSize;

    @Value("${tour.guide.concurrency.executor.max.pool.size}")
    private String maxCoreSize;

    @Bean
    public Executor taskExecutor()
    {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setThreadNamePrefix("TourGuide-");
        executor.initialize();

        return executor;
    }
}
