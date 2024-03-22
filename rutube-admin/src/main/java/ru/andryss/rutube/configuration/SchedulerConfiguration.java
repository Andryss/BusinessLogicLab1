package ru.andryss.rutube.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    @Bean
    public ScheduledExecutorService schedulerExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }

}
