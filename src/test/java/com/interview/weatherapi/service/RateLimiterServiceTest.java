package com.interview.weatherapi.service;

import com.interview.weatherapi.exception.RateLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class RateLimiterServiceTest {

    private RateLimiterService rateLimiterService;

    int requestLimit = 5;
    int limitPeriod = 3600000;

    String apiKey = "testApiKey";
    String apiKeySecondary = "testApiKey2";

    @Mock
    private TimeService timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rateLimiterService = new RateLimiterService(limitPeriod, requestLimit, timeService);
        when(timeService.getEpochMilli()).thenReturn(0L);
    }

    @Test
    public void testValidateCanProceed_withinLimit() {
        //TODO remove code duplication with helper method that takes an apiKey and number of times to run requests.
        // This will make the code more readable
        IntStream.range(0, requestLimit - 1).forEach(i -> rateLimiterService.validateCanProceed(apiKey));

        assertDoesNotThrow(() -> rateLimiterService.validateCanProceed(apiKey));
    }

    @Test
    public void testValidateCanProceed_exceedsLimit() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));

        assertThrows(RateLimitException.class, () -> rateLimiterService.validateCanProceed(apiKey));
    }

    @Test
    public void testValidateCanProceed_secondKeyWithinLimit() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));
        IntStream.range(0, requestLimit - 1).forEach(i -> rateLimiterService.validateCanProceed(apiKeySecondary));

        assertDoesNotThrow(() -> rateLimiterService.validateCanProceed(apiKeySecondary));
    }

    @Test
    public void testValidateCanProceed_secondKeyExceedsLimit() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKeySecondary));

        assertThrows(RateLimitException.class, () -> rateLimiterService.validateCanProceed(apiKeySecondary));
    }


    @Test
    public void testValidateCanProceed_exceedsLimitBeforePeriodBoundary() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));

        when(timeService.getEpochMilli()).thenReturn(limitPeriod - 1L);

        assertThrows(RateLimitException.class, () -> rateLimiterService.validateCanProceed(apiKey));
    }

    @Test
    public void testValidateCanProceed_exceedsLimitAtPeriodBoundary() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));

        when(timeService.getEpochMilli()).thenReturn(Long.valueOf(limitPeriod));

        assertThrows(RateLimitException.class, () -> rateLimiterService.validateCanProceed(apiKey));
    }

    @Test
    public void testValidateCanProceed_withinLimitAfterPeriodBoundary() {
        IntStream.range(0, requestLimit).forEach(i -> rateLimiterService.validateCanProceed(apiKey));

        when(timeService.getEpochMilli()).thenReturn(limitPeriod + 2L);

        assertDoesNotThrow(() -> rateLimiterService.validateCanProceed(apiKey));
    }

}