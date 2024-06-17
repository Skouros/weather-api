package com.interview.weatherapi.advice;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Value
public class ErrorDetails {
    String message;
    LocalDateTime timestamp;
}