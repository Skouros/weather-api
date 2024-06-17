package com.interview.weatherapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = "[a-zA-Z]{2}", message = "Country code must be exactly 2 letters")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
public @interface ValidCountryCode {

    String message() default "Invalid country code or not yet supported";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
