package com.example.Windsurfer.controller;

import com.example.Windsurfer.data.Location;
import com.example.Windsurfer.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    public Mono<ResponseEntity<String>> getWeatherForecast(@RequestParam String location, @RequestParam String state) {
        return weatherService.getWeatherData(location, state)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Error retrieving weather data: " + e.getMessage())));
    }

    @GetMapping("/getMultipleCities")
    public  ResponseEntity<Object> getMultipleCities() {
        try {
            List<Location> result = weatherService.getMultipleLocations();
            return ResponseEntity.ok(result);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error" + e.getMessage());
        }
    }
}