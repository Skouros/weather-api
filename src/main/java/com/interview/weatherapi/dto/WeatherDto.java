package com.interview.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class WeatherDto {

    @JsonProperty("name")
    String cityName;

    @JsonProperty("weather")
    List<WeatherDescriptionDto> weatherDescriptions;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @JsonProperty("dt")
    Instant measurementTimestamp;

    @JsonProperty("sys")
    SystemDto systemDto;

}