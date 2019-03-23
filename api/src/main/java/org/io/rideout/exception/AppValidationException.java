package org.io.rideout.exception;

import java.util.ArrayList;

public class AppValidationException extends RuntimeException {

    private ArrayList<String> errors;

    public AppValidationException(ArrayList<String> errors) {
        this.errors = errors;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
