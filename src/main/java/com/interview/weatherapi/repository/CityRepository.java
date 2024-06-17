package com.interview.weatherapi.repository;

import com.interview.weatherapi.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<Weather, Long> {
}
