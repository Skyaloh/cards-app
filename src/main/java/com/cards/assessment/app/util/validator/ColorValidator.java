package com.cards.assessment.app.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColorValidator  implements ConstraintValidator<ValidColor, String> {
    @Override
    public void initialize(ValidColor constraintAnnotation) {
        // Not needed for this example
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.toLowerCase().startsWith("#") && value.length() <= 6
                && value.matches("#[0-9a-fA-F]+");
    }
}
