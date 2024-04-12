package com.example.aggregatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class AggregatorServiceApplication {

   

    public static void main(String[] args) {
        SpringApplication.run(AggregatorServiceApplication.class, args);
        
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
 }
  

