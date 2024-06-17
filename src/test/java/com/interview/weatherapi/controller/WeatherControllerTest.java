package com.interview.weatherapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private String apiKey = "Rk1V6bYrAxk4t1Fsyx0M7tzyhy+bZQvJvCTB9MUkCbc=";

    @BeforeEach
    public void setup() {
        // TODO read json from a file
        String jsonResponse = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":291.26,\"feels_like\":290.75,\"temp_min\":289.18,\"temp_max\":292.34,\"pressure\":1005,\"humidity\":62},\"visibility\":10000,\"wind\":{\"speed\":5.66,\"deg\":220},\"clouds\":{\"all\":56},\"dt\":1718562545,\"sys\":{\"type\":2,\"id\":2091269,\"country\":\"GB\",\"sunrise\":1718509362,\"sunset\":1718569195},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";

        Mockito.when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenReturn(jsonResponse);
    }

    @Test
    public void testGetWeatherValidation() throws Exception {
        //TODO split up test case and verify response body
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/London/UK")
                        .header("X-API-Key", apiKey))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/London/123")
                        .header("X-API-Key", apiKey))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/Lo&ndon/GB")
                        .header("X-API-Key", apiKey))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // TODO test api key

    // TODO test rate limiting

    // TODO test what happens when saving existing weather description? I suspect it will fail due unique key violation. Maybe fetch first
    //  This is very important as this is likely broken, I just haven't manually hit triggering data yet. Maybe test in WeatherServiceTest
    //  Note: this can be easily tested from here by modifying the json timestamps above and running 2 tests. But its better to test the discrete unit

}