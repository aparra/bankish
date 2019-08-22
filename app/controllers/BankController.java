package controllers;

import bank.Banker;
import dtos.CreateAccountRequest;
import dtos.TransferRequest;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

@Singleton
public class BankController extends Controller {

    private Banker banker;

    @Inject
    public BankController(Banker banker) {
        this.banker = banker;
    }

    public CompletionStage<Result> create(Http.Request request) {
        return RequestHelper.parse(request, CreateAccountRequest.class)
                .thenCompose(accountRequest -> banker.createAccount(accountRequest))
                .thenApply(response -> ok(Json.toJson(response)))
                .exceptionally(ResultHelper::errorHandler);
    }

    public CompletionStage<Result> transfer(Http.Request request) {
        return RequestHelper.parse(request, TransferRequest.class)
                .thenCompose(transferRequest -> banker.transfer(transferRequest))
                .thenApply(response -> ok(Json.toJson(response)))
                .exceptionally(ResultHelper::errorHandler);
    }
}
