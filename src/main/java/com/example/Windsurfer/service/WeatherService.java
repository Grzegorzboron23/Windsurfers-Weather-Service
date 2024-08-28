package com.example.Windsurfer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey;

    public WeatherService(WebClient.Builder webClientBuilder,
                          @Value("${weatherbit.api.key}") String apiKey,
                          @Value("${weatherbit.api.base-url}") String basicURL) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder.baseUrl(basicURL).build();
    }

    public Mono<String> getWeatherData(String city, String state) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2.0/forecast/daily")
                        .queryParam("city", city + "," + state)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}



