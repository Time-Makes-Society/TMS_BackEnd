package com.project.tms.config;


import com.project.tms.scheduler.CategoryPollingScheduler;
import com.project.tms.scheduler.FlaskRequestSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SchedulerConfig {

    @Value("http://localhost:8080")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FlaskRequestSender flaskRequestSender(RestTemplate restTemplate) {
        return new FlaskRequestSender(restTemplate, baseUrl);
    }

    @Bean
    public CategoryPollingScheduler categoryPollingScheduler(RestTemplate restTemplate) {
        return new CategoryPollingScheduler(restTemplate, baseUrl);
    }
}
