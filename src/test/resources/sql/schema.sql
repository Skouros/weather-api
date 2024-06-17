CREATE TABLE weather
(
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    city_name                   VARCHAR(100) NOT NULL,
    country_code                VARCHAR(2)   NOT NULL,
    user_submitted_country_code VARCHAR(2)   NOT NULL,
    measurement_timestamp       TIMESTAMP    NOT NULL,
    UNIQUE (city_name, country_code, user_submitted_country_code, measurement_timestamp)
);

CREATE TABLE weather_description
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_description VARCHAR(255)  NOT NULL,
    description       VARCHAR(1000) NOT NULL,
    icon              VARCHAR(10)   NOT NULL
);

CREATE TABLE weather_weather_description
(
    weather_id             BIGINT,
    weather_description_id BIGINT,
    description_order      INT NOT NULL,
    PRIMARY KEY (weather_id, weather_description_id),
    FOREIGN KEY (weather_id) REFERENCES weather (id),
    FOREIGN KEY (weather_description_id) REFERENCES weather_description (id)
);