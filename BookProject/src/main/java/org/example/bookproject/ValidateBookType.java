package org.example.bookproject;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DictionaryValidator.class)
public @interface ValidateBookType {

    String message() default "Invalid book type: It should be either 'Novel' or 'Action'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
