package com.interview.weatherapi.advice;

import com.interview.weatherapi.exception.InvalidApiKeyException;
import com.interview.weatherapi.exception.RateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<ErrorDetails> handleInvalidApiKeyException(InvalidApiKeyException exception, WebRequest request) {
        // Intentionally not logging the Api Key as this could be a security concern
        logger.info("Invalid API key. Request description: {}", request.getDescription(false), exception);

        ErrorDetails errorDetails = new ErrorDetails(
                "Invalid Api Key", LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorDetails> handleRateLimitException(RateLimitException exception, WebRequest request) {
        logger.info("Rate Limit Exceeded. Request description: {}", request.getDescription(false), exception);

        ErrorDetails errorDetails = new ErrorDetails("Rate Limit Exceeded", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.TOO_MANY_REQUESTS);
    }

    // TODO handle this better when validation is working properly i.e. make sure response is useful. I expect that letting this through
    // for spring to pick up would give a good response, but I don't think that's the case
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request) {
        logger.error("Invalid request parameters. Request description: {}", request.getDescription(false), exception);

        ErrorDetails errorDetails = new ErrorDetails("Invalid request parameters", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        logger.error("Invalid argument type. Request description: {}", request.getDescription(false), exception);

        ErrorDetails errorDetails = new ErrorDetails("Invalid argument type", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest request) {
        logger.error("Unhandled Global Exception. Request description: {}", request.getDescription(false), exception);

        ErrorDetails errorDetails = new ErrorDetails("Internal server error", LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}