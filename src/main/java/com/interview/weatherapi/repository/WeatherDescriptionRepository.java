package com.interview.weatherapi.repository;

import com.interview.weatherapi.entity.WeatherDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDescriptionRepository extends JpaRepository<WeatherDescription, Long> {
}
