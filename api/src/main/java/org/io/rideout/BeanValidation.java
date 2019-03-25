package org.io.rideout;

import org.io.rideout.exception.ValidationException;
import org.io.rideout.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class BeanValidation {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private BeanValidation() {}

    public static <T> void validate(T entity) {
        ArrayList<String> errors = new ArrayList<>();

        if (entity instanceof User) {
            errors = validateUser((User) entity);
        }

        Set<ConstraintViolation<T>> violations = BeanValidation.validator.validate(entity);

        if (!violations.isEmpty()) {
            errors.addAll(convertViolations(violations));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static ArrayList<String> validateUser(User user) {
        ArrayList<String> result = new ArrayList<>();

        if (user.getDateOfBirth() != null) {
            if (user.getRole().equals(User.RIDER)) {
                if (!validateDate(user.getDateOfBirth(), 16)) {
                    result.add("Rider must be at least 16 years old");
                }
            } else if (user.getRole().equals(User.STAFF)) {
                if (!validateDate(user.getDateOfBirth(), 18)) {
                    result.add("Staff must be at least 18 years old");
                }
            }
        }

        return result;
    }

    private static boolean validateDate(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years * -1);

        return date.getTime() <= calendar.getTimeInMillis();
    }

    private static <T> ArrayList<String> convertViolations(Set<ConstraintViolation<T>> violations) {
        ArrayList<String> errors = new ArrayList<>();

        for (ConstraintViolation<T> violation : violations) {
            errors.add(violation.getPropertyPath() + " " + violation.getMessage());
        }

        return errors;
    }
}
