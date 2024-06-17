package com.interview.weatherapi.service;

import com.interview.weatherapi.exception.InvalidApiKeyException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiKeyValidatorService {
    //Keys generated from 32 byte secure random, Base64 encoded
    private static final List<String> VALID_API_KEYS = Arrays.asList(
            "Rk1V6bYrAxk4t1Fsyx0M7tzyhy+bZQvJvCTB9MUkCbc=",
            "7bcfj/ynoG9KrBs0p4EM44izzCVuQxj2LrpBbn1Byuk=",
            "j1ka7ll5YVpQaGxPPRmFShHc66tUwjVxAZlxCpJILMw=",
            "79upsvUXdFSlSGfqex8/+Z977JZi3EnpcbrKefjvwbY=",
            "jR1RjZNdFLcu0Hj/QLd7yHYgXHqdKShIrUdGnV3rKsU="
    );

    public void validateApiKey(String apiKey) {
        if (!VALID_API_KEYS.contains(apiKey)) {
            throw new InvalidApiKeyException();
        }
    }

}
