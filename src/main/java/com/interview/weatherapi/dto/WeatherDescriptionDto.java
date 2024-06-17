package com.interview.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WeatherDescriptionDto {

    @JsonProperty("id")
    // Mapping the id from the request as this has meaning (as per openweathermap documentation), and helps with the many to many mapping
    Long id;

    @JsonProperty("main")
    String shortDescription;

    @JsonProperty("description")
    String description;

    @JsonProperty("icon")
    String icon;

}
