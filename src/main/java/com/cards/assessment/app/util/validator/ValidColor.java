package com.cards.assessment.app.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColorValidator.class)
@Documented
public @interface ValidColor {

    String message() default "Invalid color. Color name should start with # and 6 characters atmost";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
