package com.interview.weatherapi.repository;

import com.interview.weatherapi.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w " +
            "WHERE (w.countryCode = :countryCode OR w.userSubmittedCountryCode = :countryCode) " +
            "AND w.cityName = :cityName " +
            "ORDER BY w.measurementTimestamp DESC")
    List<Weather> findWeatherByCityNameAndCountryCode(@Param("cityName") String cityName,
                                                      @Param("countryCode") String countryCode, Pageable pageable);
}
