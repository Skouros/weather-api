package com.interview.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.weatherapi.dto.WeatherDto;
import com.interview.weatherapi.entity.Weather;
import com.interview.weatherapi.entity.WeatherDescription;
import com.interview.weatherapi.mapper.WeatherMapper;
import com.interview.weatherapi.model.WeatherResponse;
import com.interview.weatherapi.repository.WeatherDescriptionRepository;
import com.interview.weatherapi.repository.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeService {

    public long getEpochMilli() {
        return Instant.now().toEpochMilli();
    }

}
