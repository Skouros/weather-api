package com.interview.weatherapi.service;

import com.interview.weatherapi.exception.RateLimitException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A very crude rate limiting service. Ideally, this should be backed by a distributed cache to account for request on
 * other application instances.
 * <p>
 * Also, the use of synchronized could be problematic on high volume services
 */
@Service
public class RateLimiterService {
    private final Map<String, Deque<Long>> requestTokens = new HashMap<>();
    private final TimeService timeService;

    int requestLimit;
    int limitPeriod;

    public RateLimiterService(@Value("${rate-limiter.period-milliseconds}") int limitPeriod,
                              @Value("${rate-limiter.request-limit}") int requestLimit, TimeService timeService) {
        this.limitPeriod = limitPeriod;
        this.requestLimit = requestLimit;
        this.timeService = timeService;
    }

    /**
     * Only rate limits by ApiKey. There is room to extend and limit by and endpoint key should further endpoints
     * be added. That being said openweathermaps is also limited by user not endpoint - multiple keys owned by the same
     * user are tracked and combined for rate limiting metrics.
     *
     * @param apiKey key representing the user whose requests are rate limited
     */
    public synchronized void validateCanProceed(String apiKey) {
        long now = timeService.getEpochMilli();
        Deque<Long> tokens = requestTokens.computeIfAbsent(apiKey, k -> new ArrayDeque<>());

        // Remove tokens that are older than 1 hour
        while (!tokens.isEmpty() && tokens.peek() < now - limitPeriod) {
            tokens.poll();
        }

        // Check if rate limit is exceeded
        if (tokens.size() > requestLimit - 1) {
            throw new RateLimitException();
        }

        tokens.offer(now); //Add current request
    }
}
