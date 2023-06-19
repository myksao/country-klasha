package com.myk.countryklasha;

import com.myk.countryklasha.annotation.ValuesAllowed;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, String> {

    private List<String> expectedValues;
    private String returnMessage;

    @Override
    public void initialize(ValuesAllowed requiredIfChecked) {
        expectedValues = Arrays.asList(requiredIfChecked.values());
        returnMessage = requiredIfChecked.message().concat(expectedValues.toString());
    }

    @Override
    public boolean isValid(String testValue, ConstraintValidatorContext context) {
        boolean valid = expectedValues.contains(testValue);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(returnMessage)
                    .addConstraintViolation();
        }
        return valid;
    }
}
