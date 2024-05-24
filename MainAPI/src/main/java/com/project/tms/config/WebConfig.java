package com.project.tms.config;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component("webConfiguration")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);


        registry.addMapping("/**")
                .allowedOrigins(
                        "https://heartfelt-baklava-2f7a5f.netlify.app",
                        "https://timemakessociety.vercel.app",
                        "https://www.tms-official.site",
                        "http://localhost:3000",
                        "http://localhost:5173"
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
