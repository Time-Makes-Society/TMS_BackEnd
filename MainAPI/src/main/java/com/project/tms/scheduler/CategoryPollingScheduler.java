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
            sender.sendRequest("연예");
            sender.sendRequest("정치");
            sender.sendRequest("과학");
            sender.sendRequest("사회");
            sender.sendRequest("스포츠");
            sender.sendRequest("기술");
            sender.sendRequest("해외");
            // 나머지 카테고리에 대한 요청 추가
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
