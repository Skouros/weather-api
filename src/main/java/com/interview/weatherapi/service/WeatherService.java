package com.interview.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.weatherapi.dto.WeatherDto;
import com.interview.weatherapi.entity.Weather;
import com.interview.weatherapi.entity.WeatherDescription;
import com.interview.weatherapi.mapper.WeatherMapper;
import com.interview.weatherapi.model.WeatherResponse;
import com.interview.weatherapi.repository.WeatherDescriptionRepository;
import com.interview.weatherapi.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WeatherDescriptionRepository weatherDescriptionRepository;

    @Value("${open-weather-map.api-key}")
    private String apiKey;

    @Value("${open-weather-map.name-api-url}")
    private String apiUrl;

    @Value("${weather.cache-duration-minutes}")
    private int cacheDurationMinutes;

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, WeatherMapper weatherMapper, RestTemplate restTemplate,
                          ObjectMapper objectMapper, WeatherDescriptionRepository weatherDescriptionRepository) {
        this.weatherRepository = weatherRepository;
        this.weatherMapper = weatherMapper;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.weatherDescriptionRepository = weatherDescriptionRepository;
    }

    public Weather saveWeather(WeatherDto weatherDto, String submittedCountryCode) {
        Weather weatherEntity = weatherMapper.mapToEntity(weatherDto);
        weatherEntity.setUserSubmittedCountryCode(submittedCountryCode);
        List<WeatherDescription> weatherDescriptions = weatherDescriptionRepository.saveAll(weatherEntity.getWeatherDescriptions());
        weatherEntity.setWeatherDescriptions(weatherDescriptions);
        return weatherRepository.save(weatherEntity);
    }

    public WeatherResponse getWeather(String cityName, String countryCode) {
        Weather weather = getCachedWeatherData(cityName, countryCode);

        // Consider data stale after 15 minutes as openweathermaps refreshes their data between 15 and 20 minutes for this endpoint
        if (isWeatherDataStaleOrNotFound(weather)) {
            // TODO check if stale data is useful or if I should delete
            WeatherDto weatherDto = fetchWeatherDataFromApi(cityName, countryCode);
            weather = saveWeather(weatherDto, countryCode);
        }

        // TODO Handle not found scenario, rate limiting and other statuses, in controller advice and unit test

        return convertToWeatherResponse(weather);
    }

    private Weather getCachedWeatherData(String cityName, String countryCode) {
        Weather result = null;

        // TODO replace this with QueryDsl or something else (preferably not a native query). The problem here is that it
        //  does an extra query, i.e. a count query
        List<Weather> weathers = weatherRepository.findWeatherByCityNameAndCountryCode(cityName, countryCode, PageRequest.of(0, 1));
        if (!weathers.isEmpty()) {
            result = weathers.get(0);
        }

        return result;
    }

    private boolean isWeatherDataStaleOrNotFound(Weather weather) {
        return weather == null || weather.getMeasurementTimestamp().isBefore(Instant.now().minus(cacheDurationMinutes, ChronoUnit.MINUTES));
    }

    private WeatherDto fetchWeatherDataFromApi(String cityName, String countryCode) {
        String response;

        String url = buildWeatherApiUrl(cityName, countryCode);
        try {
            response = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            //TODO will also be logged by Controller advice - have to give this some thought.
            // Throw a special type so the controller advice can ignore it? Remove this catch block?
            logger.error("Failed to retrieve weather data from {}", url, e);
            throw e;
        }

        return parseWeatherResponse(response);
    }


    private String buildWeatherApiUrl(String cityName, String countryCode) {
        // TODO This lib has a vulnerability. Potentially have to sanitise the input to prevent path traversals or
        //  check the canonical path against the derived one. This is even more dangerous since validation isn't working.
        //  Even if I use another library, it would be good to check for path traversals / ssrf
        return UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", cityName + "," + countryCode)
                .queryParam("APPID", apiKey)
                .toUriString();
    }

    private WeatherDto parseWeatherResponse(String response) {
        try {
            return objectMapper.readValue(response, WeatherDto.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed parse Weather data response retrieve weather data. Data: {}", response, e);
            throw new RuntimeException(e);
        }
    }

    private WeatherResponse convertToWeatherResponse(Weather weather) {
        List<String> descriptions = weather.getWeatherDescriptions()
                .stream()
                .map(WeatherDescription::getDescription)
                .collect(Collectors.toList());

        return new WeatherResponse(descriptions);
    }
}
