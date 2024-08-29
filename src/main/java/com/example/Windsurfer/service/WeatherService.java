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
import java.sql.SQLOutput;
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
        locations.add(new Location(8872468, "La Fortaleza"));
        locations.add(new Location(146150, "Pissoúri"));
        locations.add(new Location(3570423, "Le Morne-Rouge"));
        return locations;
    }

    public List<Location> getMultipleLocations() throws IOException, InterruptedException {
        List<Location> locations = initializeLocations();
        List<String> responses = weatherAPIConnection.connectToAPIMultipleCity(locations);


        System.out.println("MultipleLocations before");
        for (String currentData : responses) {
            JSONObject currentObject = new JSONObject(currentData);
            System.out.println("city name" + currentObject.getString("city_name"));
            String cityName = currentObject.getString("city_name");

            for (Location location : locations) {
                if (location.getCity().equals(cityName)) {
                    JSONArray dataArray = currentObject.getJSONArray("data");
                    System.out.println("GetJsonObject 0 " +  dataArray.getJSONObject(0));
                    JSONObject cityData = dataArray.getJSONObject(0);
                    location.setTemperature(cityData.getDouble("temp"));
                    location.setWindSpeed(cityData.getDouble("wind_spd"));
                    break;
                }
            }
        }

        return locations;
    }


    public List<Location> findBestWindsurfingLocation() throws IOException, InterruptedException {
        List<Location> locations = getMultipleLocations();

        System.out.println("Find best findBestWindsurfingLocation");
        System.out.println("Locations " + locations);

        Location bestLocation = null;
        List<Location> suitableLocations = new ArrayList<>();
        double highestScore = Double.NEGATIVE_INFINITY;

        for (Location location : locations) {
            System.out.println("Temperature " +location.getTemperature());
            System.out.println("getWindSpeed " +location.getWindSpeed());
            double temperature = location.getTemperature();
            double windSpeed = location.getWindSpeed();

            if (temperature >= 5 && temperature <= 35 && windSpeed >= 5 && windSpeed <= 18) {
                double score = windSpeed * 3 + temperature;
                if (score > highestScore) {
                    highestScore = score;
                    bestLocation = location;
                }
            }else{
                suitableLocations.add(location);
            }
        }

        System.out.println("Suitbale " + suitableLocations);
        System.out.println("Best " + bestLocation);

        if(suitableLocations.isEmpty()){
            return List.of(bestLocation);
        }

        return suitableLocations;
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



