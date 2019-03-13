package org.io.rideout;

import org.io.rideout.exception.AppValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Set;

public class BeanValidation {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private BeanValidation() {}

    public static <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = BeanValidation.validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new AppValidationException(extractViolations(violations));
        }
    }

    private static  <T> ArrayList<String> extractViolations(Set<ConstraintViolation<T>> violations) {
        ArrayList<String> errors = new ArrayList<>();

        for (ConstraintViolation<T> violation : violations) {
            errors.add(violation.getPropertyPath() + " " + violation.getMessage());
        }

        return errors;
    }
}
