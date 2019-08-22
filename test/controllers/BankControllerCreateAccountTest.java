package controllers;

import dtos.AccountResponse;
import dtos.CreateAccountRequest;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static helpers.Fixtures.createAccountRequest;
import static helpers.TestHelpers.contentAsTypeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class BankControllerCreateAccountTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void should_successfully_create_an_account() {
        String holder = "Anderson";
        CreateAccountRequest createAccountRequest = createAccountRequest(holder);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(createAccountRequest))
                .uri("/api/v1/account");

        Result result = route(app, request);
        assertEquals(OK, result.status());

        AccountResponse accountResponse = contentAsTypeClass(result, AccountResponse.class);
        assertNotNull(accountResponse.id);
        assertEquals(holder, accountResponse.holder);
        assertEquals(createAccountRequest.firstDepositAmount.toPlainString(), accountResponse.balance);
    }

    @Test
    public void should_not_create_an_account_when_request_is_incomplete() {
        CreateAccountRequest incompleteAccountRequest = new CreateAccountRequest();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(incompleteAccountRequest))
                .uri("/api/v1/account");

        Result result = route(app, request);
        assertEquals(INTERNAL_SERVER_ERROR, result.status());
    }
}
