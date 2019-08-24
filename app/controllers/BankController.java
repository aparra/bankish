package controllers;

import bank.Banker;
import dtos.CreateAccountRequest;
import dtos.TransferRequest;
import play.data.Form;
import play.data.FormFactory;
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
    private FormFactory formFactory;

    @Inject
    public BankController(Banker banker, FormFactory formFactory) {
        this.banker = banker;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> create(Http.Request request) {
        return RequestHelper.parseWith(formFactory.form(CreateAccountRequest.class), request)
                .thenCompose(accountRequest -> banker.createAccount(accountRequest))
                .thenApply(response -> ok(Json.toJson(response)))
                .exceptionally(ResultHelper::errorHandler);
    }

    public CompletionStage<Result> transfer(Http.Request request) {
        return RequestHelper.parseWith(formFactory.form(TransferRequest.class), request)
                .thenCompose(transferRequest -> banker.transfer(transferRequest))
                .thenApply(response -> ok(Json.toJson(response)))
                .exceptionally(ResultHelper::errorHandler);
    }
}
