package org.io.rideout.exception;

import java.util.ArrayList;

public class ValidationException extends RuntimeException {

    private ArrayList<String> errors;

    public ValidationException(ArrayList<String> errors) {
        this.errors = errors;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
