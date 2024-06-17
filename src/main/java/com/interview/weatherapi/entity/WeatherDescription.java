package com.interview.weatherapi.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class WeatherDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String shortDescription;

    @Column(length = 1000)
    private String description;

    @Column(length = 10)
    private String icon;

}
