package controllers;

import dtos.AccountResponse;
import dtos.TransferRequest;
import dtos.TransferResponse;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.math.BigDecimal;
import java.util.UUID;

import static helpers.Fixtures.createAccountRequest;
import static helpers.Fixtures.createTransferRequest;
import static helpers.TestHelpers.contentAsTypeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class BankControllerTransferTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void should_successfully_make_a_transfer() {
        AccountResponse fromAccount = createAccountFor("Anderson From");
        AccountResponse toAccount = createAccountFor("Anderson To");
        BigDecimal transferAmount = new BigDecimal("5.00");

        TransferRequest transferRequest = createTransferRequest(fromAccount.id, toAccount.id, transferAmount);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(transferRequest))
                .uri("/api/v1/account/transfer");

        Result result = route(app, request);
        assertEquals(OK, result.status());

        TransferResponse transferResponse = contentAsTypeClass(result, TransferResponse.class);
        assertNotNull(transferResponse.at);
        assertEquals(transferRequest.from, transferResponse.from);
        assertEquals(transferRequest.to, transferResponse.to);
        assertEquals(transferAmount.toPlainString(), transferResponse.amount);
    }

    @Test
    public void should_not_make_a_transfer_when_there_is_no_enough_balance() {
        AccountResponse fromAccount = createAccountFor("Anderson From");
        AccountResponse toAccount = createAccountFor("Anderson To");
        BigDecimal transferAmount = new BigDecimal("1500.00");

        TransferRequest transferRequest = createTransferRequest(fromAccount.id, toAccount.id, transferAmount);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(transferRequest))
                .uri("/api/v1/account/transfer");

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());

        String expectedMessage = String.format("{\"message\":\"Insufficient balance in %s\"}", transferRequest.from);
        assertEquals(expectedMessage, contentAsString(result));
    }

    @Test
    public void should_not_make_a_transfer_when_account_from_does_not_exist() {
        UUID fromAccountId = UUID.randomUUID();
        AccountResponse toAccount = createAccountFor("Anderson To");
        BigDecimal transferAmount = new BigDecimal("5.00");

        TransferRequest transferRequest = createTransferRequest(fromAccountId.toString(), toAccount.id, transferAmount);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(transferRequest))
                .uri("/api/v1/account/transfer");

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());

        String expectedMessage = String.format("{\"message\":\"Account %s was not found\"}", transferRequest.from);
        assertEquals(expectedMessage, contentAsString(result));
    }

    @Test
    public void should_not_make_a_transfer_when_account_to_does_not_exist() {
        AccountResponse fromAccount = createAccountFor("Anderson From");
        UUID toAccountId = UUID.randomUUID();
        BigDecimal transferAmount = new BigDecimal("5.00");

        TransferRequest transferRequest = createTransferRequest(fromAccount.id, toAccountId.toString(), transferAmount);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(transferRequest))
                .uri("/api/v1/account/transfer");

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());

        String expectedMessage = String.format("{\"message\":\"Account %s was not found\"}", transferRequest.to);
        assertEquals(expectedMessage, contentAsString(result));
    }

    private AccountResponse createAccountFor(String holder) {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(createAccountRequest(holder)))
                .uri("/api/v1/account");

        Result result = route(app, request);
        return contentAsTypeClass(result, AccountResponse.class);
    }
}
