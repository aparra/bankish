package controllers;

import play.mvc.Http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

class RequestHelper {

    static <T> CompletionStage<T> parse(Http.Request request, Class<T> targetClass) {
        return CompletableFuture.supplyAsync(() -> request.body().parseJson(targetClass).orElseThrow(RuntimeException::new));
    }
}
