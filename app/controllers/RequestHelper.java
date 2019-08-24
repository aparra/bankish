package controllers;

import play.data.Form;
import play.mvc.Http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

interface RequestHelper {

    static <T> CompletionStage<T> parseWith(Form<T> validator, Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            Form<T> validation = validator.bindFromRequest(request);
            if (validation.hasErrors()) throw new ValidationError(validation.errorsAsJson());
            return validation.get();
        });
    }
}
