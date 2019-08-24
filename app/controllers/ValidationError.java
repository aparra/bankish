package controllers;

import com.fasterxml.jackson.databind.JsonNode;

public class ValidationError extends RuntimeException {

    public ValidationError(JsonNode error) {
        super(error.toString());
    }
}
