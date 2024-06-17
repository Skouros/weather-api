package com.interview.weatherapi.mapper;

import com.interview.weatherapi.dto.WeatherDto;
import com.interview.weatherapi.entity.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mappings({
            @Mapping(target = "countryCode", source = "dto.systemDto.countryCode")
    })
    Weather mapToEntity(WeatherDto dto);
}