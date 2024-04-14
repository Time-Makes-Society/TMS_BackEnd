package com.project.tms.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Component
public class CategoryPollingScheduler {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public CategoryPollingScheduler(RestTemplate restTemplate, @Value("$http://localhost:8080") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void pollCategories() {
        try {
            FlaskRequestSender sender = new FlaskRequestSender(restTemplate, baseUrl);
            sender.sendRequest("문화");
            sender.sendRequest("경제");
            // 나머지 카테고리에 대한 요청 추가
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
