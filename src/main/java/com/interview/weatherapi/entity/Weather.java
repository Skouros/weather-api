package com.interview.weatherapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String cityName;

    @Column(length = 2)
    private String countryCode;

    // This column is needed because submitting UK returns GB (there could be other mapping done). Subsequent UK requests
    // cause a 'cache' miss
    @Column(length = 2)
    private String userSubmittedCountryCode;

    private Instant measurementTimestamp;

    @ManyToMany
    @JoinTable(
            name = "weather_weather_description",
            joinColumns = @JoinColumn(name = "weather_id"),
            inverseJoinColumns = @JoinColumn(name = "weather_description_id")
    )

    // This column is needed because the description order is important i.e. the first description is the primary
    @OrderColumn(name = "description_order")
    private List<WeatherDescription> weatherDescriptions;
}