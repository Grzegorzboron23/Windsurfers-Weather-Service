package com.example.Windsurfer.service;

import com.example.Windsurfer.Utils.WeatherAPIConnection;
import com.example.Windsurfer.data.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WeatherServiceTests {

    @Mock
    private WeatherAPIConnection weatherAPIConnection;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeLocations() {
        List<Location> locations = weatherService.getPredefinedLocations();
        assertNotNull(locations);
        assertEquals(5, locations.size());

        assertEquals(3097421, locations.get(0).getCityID(), "The city ID should be 3097421 for the first location.");
        assertEquals("Jastarnia", locations.get(0).getCity(), "The city name should be 'Jastarnia' for the first location.");

        assertEquals(4507067, locations.get(1).getCityID(), "The city ID should be 4507067 for the second location.");
        assertEquals("Bridgetown", locations.get(1).getCity(), "The city name should be 'Bridgetown' for the second location.");

        assertEquals(8872468, locations.get(2).getCityID(), "The city ID should be 8872468 for the third location.");
        assertEquals("La Fortaleza", locations.get(2).getCity(), "The city name should be 'La Fortaleza' for the third location.");

        assertEquals(146150, locations.get(3).getCityID(), "The city ID should be 146150 for the fourth location.");
        assertEquals("Pissoúri", locations.get(3).getCity(), "The city name should be 'Pissoúri' for the fourth location.");

        assertEquals(3570423, locations.get(4).getCityID(), "The city ID should be 3570423 for the fifth location.");
        assertEquals("Le Morne-Rouge", locations.get(4).getCity(), "The city name should be 'Le Morne-Rouge' for the fifth location.");
    }

    @Test
    void testIndividualLocationProperties() {
        List<Location> locations = weatherService.getPredefinedLocations();

        // Check each location for non-null properties
        for (Location location : locations) {
            assertFalse(location.getCity().isEmpty(), "City name should not be empty.");
        }
    }

}
