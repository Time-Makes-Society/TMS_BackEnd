package com.project.tms.scheduler;

import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

public class FlaskRequestSender {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FlaskRequestSender(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void sendRequest(String category) throws UnsupportedEncodingException {
        String url = baseUrl + "/api/firstAPI/" + category;
        restTemplate.getForObject(url, String.class);
    }
}
