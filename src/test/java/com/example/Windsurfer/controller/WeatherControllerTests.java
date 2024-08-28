package com.example.Windsurfer.controller;


import com.example.Windsurfer.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        when(weatherService.getWeatherData("Warsaw", "PL"))
                .thenReturn(Mono.just("fake response"));
    }

    @Test
    void shouldReturnOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/weather/forecast")
                        .param("location", "Warsaw")
                        .param("state", "PL"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
