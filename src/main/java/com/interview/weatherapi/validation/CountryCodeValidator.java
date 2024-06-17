package com.interview.weatherapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CountryCodeValidator implements ConstraintValidator<ValidCountryCode, String> {

    /*
     List of ISO_3166-1_alpha-2 countries. Openweathermap documentation specifies to use ISO_3166.
     Ideally this would be cached from a database or service as the list does slowly change.
     This validator was added to help prevent requests to openweathermap, however the maintenance cost may outweigh
     the benefit.
     Furthermore, the list is from wikepedia, it should be taken from the bulk data from openweathermap.
     Only leaving this in to show I know about custom validators
    */
    private static final List<String> ISO_COUNTRY_CODES = Arrays.asList("AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO",
            "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL",
            "BM", "BN", "BO", "BQ", "BQ", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH",
            "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CW", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO",
            "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE",
            "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN",
            "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE",
            "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT",
            "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR",
            "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR",
            "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA",
            "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN",
            "SO", "SR", "SS", "ST", "SV", "SX", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN",
            "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN",
            "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            // Null or empty can be considered valid (even if not valid in the use case for this challenge)
            return true;
        }

        //return ISO_COUNTRY_CODES.contains(value.toUpperCase());

        //TODO Get supported countries from OpenWeatherMap bulk data. They map UK to GB, so even using their data may npt be enough.
        // Leaving this validator here as a concept. Maybe I should force that GB is used as it is to standard (but limits user experience)?
        // There are probably more limitations to do with special codes eg XB, XH, XQ, XU, XM, QM, XV, XL, and QW probably map to UM
        return true;
    }
}

