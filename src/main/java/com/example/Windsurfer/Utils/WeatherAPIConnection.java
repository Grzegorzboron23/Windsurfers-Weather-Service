package com.example.Windsurfer.Utils;

import com.example.Windsurfer.data.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherAPIConnection {
    private final String apiKey;
    private final String baseURL;

    @Autowired
    public WeatherAPIConnection(@Value("${weatherbit.api.key}") String apiKey,
                                @Value("${weatherbit.api.base-url}") String baseURL) {
        this.apiKey = apiKey;
        this.baseURL = baseURL;
    }

    public List<String> connectToAPIMultipleCity(List<Location> locations) throws IOException, InterruptedException {
        List<String> responses = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        for (Location location : locations) {
            String url = createUrlForCity(location); // Użyj pojedynczego zapytania dla każdego miasta
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Response body for city " + location.getCity() + ": " + response.body());
                responses.add(response.body());
            } else {
                throw new IOException("Error fetching weather data for city " + location.getCity() + ": " + response.statusCode());
            }
        }

        return responses;
    }

    private String createUrlForCity(Location location) {
        return baseURL + "/v2.0/forecast/daily?city_id=" + location.getCityID() + "&key=" + apiKey;
    }

    private String createBatchUrl(List<Location> locations) {
        StringBuilder urlBuilder = new StringBuilder(baseURL + "/v2.0/forecast/daily?");
        for (Location location : locations) {
            urlBuilder.append("city_id=").append(location.getCityID()).append("&");
        }

        urlBuilder.append("key=").append(apiKey);
        return urlBuilder.toString();
    }
}
