package com.example.aggregatorservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/aggregator")
public class AggregatorController {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public AggregatorController(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    @GetMapping("/sync")
    public ResponseEntity<String> aggregateSync() {
        ResponseEntity<String> responseA = restTemplate.getForEntity("http://localhost:8081/serviceA/data", String.class);
        ResponseEntity<String> responseB = restTemplate.getForEntity("http://localhost:8082/serviceB/info", String.class);

        String result = "Aggregated data (Sync): " + responseA.getBody() + " | " + responseB.getBody();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<String>> aggregateAsync() {
        CompletableFuture<String> futureA = webClient.get()
                .uri("http://localhost:8081/serviceA/data")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();

        CompletableFuture<String> futureB = webClient.get()
                .uri("http://localhost:8082/serviceB/info")
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();

        return CompletableFuture.allOf(futureA, futureB)
                .thenApply(ignore -> {
                    String result = "Aggregated data (Async): " + futureA.join() + " | " + futureB.join();
                    return ResponseEntity.ok(result);
                });
    }
}
