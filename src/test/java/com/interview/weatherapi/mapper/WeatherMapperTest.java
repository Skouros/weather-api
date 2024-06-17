package com.interview.weatherapi.mapper;

import com.interview.weatherapi.dto.SystemDto;
import com.interview.weatherapi.dto.WeatherDescriptionDto;
import com.interview.weatherapi.dto.WeatherDto;
import com.interview.weatherapi.entity.Weather;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherMapperTest {

    private WeatherMapper weatherMapper = Mappers.getMapper(WeatherMapper.class);

    @Test
    public void testMapToEntity() {
        List<WeatherDescriptionDto> weatherDescriptions = new ArrayList<>();

        WeatherDescriptionDto weatherDescription = WeatherDescriptionDto.builder().id(1L)
                .shortDescription("Clouds")
                .description("Some cloud cover")
                .icon("01x").build();

        weatherDescriptions.add(weatherDescription);

        weatherDescription = WeatherDescriptionDto.builder().id(2L)
                .shortDescription("Sunny")
                .description("Very Sunny")
                .icon("02x").build();

        weatherDescriptions.add(weatherDescription);

        WeatherDto dto = new WeatherDto("London", weatherDescriptions, Instant.ofEpochSecond(1718509362), new SystemDto("GB"));
        Weather weather = weatherMapper.mapToEntity(dto);
        assertNotNull(weather);
        assertEquals("London", weather.getCityName(), "cityName mapped incorrectly");
        assertEquals("GB", weather.getCountryCode(), "countryCode mapped incorrectly");
        assertEquals(Instant.ofEpochSecond(1718509362), weather.getMeasurementTimestamp(), "measurementTimestamp mapped incorrectly");

        assertEquals(2, weather.getWeatherDescriptions().size(), "weatherDescriptions size is incorrect");

        // Assert first weather description
        assertEquals(1L, weather.getWeatherDescriptions().get(0).getId(), "First weather description ID mapped incorrectly");
        assertEquals("Clouds", weather.getWeatherDescriptions().get(0).getShortDescription(), "First weather description shortDescription mapped incorrectly");
        assertEquals("Some cloud cover", weather.getWeatherDescriptions().get(0).getDescription(), "First weather description description mapped incorrectly");
        assertEquals("01x", weather.getWeatherDescriptions().get(0).getIcon(), "First weather description icon mapped incorrectly");

        // Assert second weather description
        assertEquals(2L, weather.getWeatherDescriptions().get(1).getId(), "Second weather description ID mapped incorrectly");
        assertEquals("Sunny", weather.getWeatherDescriptions().get(1).getShortDescription(), "Second weather description shortDescription mapped incorrectly");
        assertEquals("Very Sunny", weather.getWeatherDescriptions().get(1).getDescription(), "Second weather description description mapped incorrectly");
        assertEquals("02x", weather.getWeatherDescriptions().get(1).getIcon(), "Second weather description icon mapped incorrectly");
    }

    // TODO Add some negative test cases e.g. null value handling
}