package com.interview.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder

// Shouldn't be needed but can't deserialise without it. Note: this isn't affecting WeatherDescriptionDto.
// I suspect this breaks lombok so I need a hashCode equals and toString
// TODO check the above
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SystemDto {

    @JsonProperty("country")
    String countryCode;

}