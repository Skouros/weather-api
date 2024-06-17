package com.interview.weatherapi.model;

import lombok.Value;

import java.util.List;

@Value
public class WeatherResponse {
    List<String> description;
}