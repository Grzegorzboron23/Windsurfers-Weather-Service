package com.example.Windsurfer.service;

import com.example.Windsurfer.Utils.WeatherAPIConnection;
import com.example.Windsurfer.data.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey;
    private final String basicURL;
    private final WeatherAPIConnection weatherAPIConnection;

    public WeatherService(WebClient.Builder webClientBuilder,
                          @Value("${weatherbit.api.key}") String apiKey,
                          @Value("${weatherbit.api.base-url}") String basicURL, WeatherAPIConnection weatherAPIConnection, WeatherAPIConnection weatherAPIConnection1) {
        this.apiKey = apiKey;
        this.weatherAPIConnection = weatherAPIConnection1;
        this.webClient = webClientBuilder.baseUrl(basicURL).build();
        this.basicURL = basicURL;
    }

    public List<Location> initializeLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(3097421, "Jastarnia"));
        locations.add(new Location(4507067, "Bridgetown"));
        locations.add(new Location(8872468, "Fortaleza"));
        locations.add(new Location(146150, "Pissouri"));
        locations.add(new Location(3570423, "Le Morne"));
        return locations;
    }

    public List<Location> getMultipleLocations() throws IOException, InterruptedException {
        List<Location> locations = initializeLocations();

        String currentData = weatherAPIConnection.connectToAPIMultipleCity(locations);
        JSONObject currentObject = new JSONObject(currentData);
        JSONArray dataArray = currentObject.getJSONArray("data");

        for (Location location : locations) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject cityData = dataArray.getJSONObject(i);
                location.setTemperature(cityData.getDouble("temp"));
                location.setWindSpeed(cityData.getDouble("wind_spd"));
            }
        }
        return locations;
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



