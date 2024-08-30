package com.example.Windsurfer.service;

import com.example.Windsurfer.Utils.WeatherAPIConnection;
import com.example.Windsurfer.data.Location;
import com.example.Windsurfer.exception.DataNotFoundException;
import com.example.Windsurfer.exception.WeatherDataProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final double MIN_TEMPERATURE = 5;
    private static final double MAX_TEMPERATURE = 35;
    private static final double MIN_WIND_SPEED = 5;
    private static final double MAX_WIND_SPEED = 18;
    private static final double WIND_SPEED_MULTIPLIER = 3;

    private final String apiKey;
    private final String basicURL;
    private final WeatherAPIConnection weatherAPIConnection;

    public WeatherService(@Value("${weatherbit.api.key}") String apiKey,
                          @Value("${weatherbit.api.base-url}") String basicURL, WeatherAPIConnection weatherAPIConnection) {
        this.apiKey = apiKey;
        this.weatherAPIConnection = weatherAPIConnection;
        this.basicURL = basicURL;
    }

    public List<Location> getPredefinedLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(3097421, "Jastarnia"));
        locations.add(new Location(4507067, "Bridgetown"));
        locations.add(new Location(8872468, "La Fortaleza"));
        locations.add(new Location(146150, "Pissoúri"));
        locations.add(new Location(3570423, "Le Morne-Rouge"));
        return locations;
    }

    public List<Location> getMultipleLocations() throws IOException, InterruptedException {
        List<Location> locations = getPredefinedLocations();
        List<String> responses = weatherAPIConnection.connectToAPIMultipleCity(locations);

        for (String currentData : responses) {
            try {
                JSONObject currentObject = new JSONObject(currentData);
                updateLocationData(locations, currentObject);
            } catch (Exception e) {
                throw new DataNotFoundException("Error ",e);
            }
        }
        return locations;
    }

    private void updateLocationData(List<Location> locations, JSONObject currentObject) {
        String cityName = currentObject.getString("city_name");

        for (Location location : locations) {
            if (location.getCity().equals(cityName)) {
                JSONArray dataArray = currentObject.getJSONArray("data");
                JSONObject cityData = dataArray.getJSONObject(0);

                location.setTemperature(cityData.getDouble("temp"));
                location.setWindSpeed(cityData.getDouble("wind_spd"));
                location.setLatitude(currentObject.getDouble("lat"));
                location.setLongitude(currentObject.getDouble("lon"));
                break;
            }
        }
    }

    public List<Location> findBestWindsurfingLocation() throws IOException, InterruptedException {
        List<Location> locations = getMultipleLocations();
        Location bestLocation = locations.stream()
                .filter(this::isSuitableForWindsurfing)
                .max((loc1, loc2) -> Double.compare(calculateLocationScore(loc1), calculateLocationScore(loc2)))
                .orElse(null);

        return bestLocation != null ? List.of(bestLocation) : List.of();
    }

    private double calculateLocationScore(Location location) {
        return location.getWindSpeed() * WIND_SPEED_MULTIPLIER + location.getTemperature();
    }

    private boolean isSuitableForWindsurfing(Location location) {
        double temperature = location.getTemperature();
        double windSpeed = location.getWindSpeed();
        return temperature >= MIN_TEMPERATURE && temperature <= MAX_TEMPERATURE &&
                windSpeed >= MIN_WIND_SPEED && windSpeed <= MAX_WIND_SPEED;
    }

    public List<Location> findBestWindsurfingLocationForDate(String date) throws IOException, InterruptedException, WeatherDataProcessingException {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }

        List<Location> locations = getPredefinedLocations();
        List<String> responses = weatherAPIConnection.connectToAPIMultipleCityForDate(locations, date);

        for (String currentData : responses) {
            try {
                JSONObject currentObject = new JSONObject(currentData);
                updateLocationData(locations, currentObject);
            } catch (Exception e) {
                throw new WeatherDataProcessingException("Failed to process weather data for location.", e);
            }
        }

        Location bestLocation = locations.stream()
                .filter(this::isSuitableForWindsurfing)
                .max((loc1, loc2) -> Double.compare(calculateLocationScore(loc1), calculateLocationScore(loc2)))
                .orElse(null);

        return bestLocation != null ? List.of(bestLocation) : List.of();
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}



