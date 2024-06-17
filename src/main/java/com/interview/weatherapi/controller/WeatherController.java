package com.interview.weatherapi.controller;

import com.interview.weatherapi.model.WeatherResponse;
import com.interview.weatherapi.service.ApiKeyValidatorService;
import com.interview.weatherapi.service.RateLimiterService;
import com.interview.weatherapi.service.WeatherService;
import com.interview.weatherapi.validation.ValidCountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final RateLimiterService rateLimiterService;
    private final ApiKeyValidatorService apiKeyValidatorService;
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(RateLimiterService rateLimiterService, ApiKeyValidatorService apiKeyValidatorService, WeatherService weatherService) {
        this.rateLimiterService = rateLimiterService;
        this.apiKeyValidatorService = apiKeyValidatorService;
        this.weatherService = weatherService;
    }

    @GetMapping("/{cityName}/{countryCode}")
    public ResponseEntity<WeatherResponse> getWeather(
            @NotEmpty(message = "City name cannot be empty")
    /*
     I wanted a regex that could handle international characters (since openweathermap handles it) as well as space,
     full stop dash and apostrophe. This seems to the trick, although it's always risky that it's too strict/permissive.

     Ideally I would parse the bulk openweathermap data to test of all the cities pass this regex.
    */
            @Pattern(regexp = "^([a-zA-Z\\u0080-\\u024F]+(?:\\. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$",
                    message = "City name is invalid or not supported")
            @PathVariable String cityName,
            @PathVariable @NotEmpty(message = "Country code cannot be empty")
            @Pattern(regexp = "[a-zA-Z]{2}", message = "Country code must be exactly 2 letters")
            @ValidCountryCode
            String countryCode,
            @RequestHeader("X-API-Key") String apiKey
    ) {
        apiKeyValidatorService.validateApiKey(apiKey);
        rateLimiterService.validateCanProceed(apiKey);

        WeatherResponse weatherResponse = weatherService.getWeather(cityName, countryCode);

        if (weatherResponse != null) {
            return ResponseEntity.ok(weatherResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}