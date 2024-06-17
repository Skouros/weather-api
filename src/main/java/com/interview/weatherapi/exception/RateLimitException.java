package com.interview.weatherapi.exception;

public class RateLimitException extends RuntimeException {

    public RateLimitException() {
        super("Rate limit exceeded");
    }

}
