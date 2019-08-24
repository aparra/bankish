package controllers;

import models.Exceptions;
import play.libs.Json;
import play.mvc.Result;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;

class ResultHelper {

    static Result errorHandler(Throwable error) {
        Throwable cause = error.getCause();

        if (cause instanceof ValidationError) {
            return badRequest(Json.parse(cause.getMessage()));
        } else if (cause instanceof Exceptions.AccountNotFoundException || cause instanceof Exceptions.InsufficientBalanceException) {
            return badRequest(Json.newObject().put("message", cause.getMessage()));
        } else {
            return internalServerError(Json.newObject().put("message", error.getMessage()));
        }
    }
}
