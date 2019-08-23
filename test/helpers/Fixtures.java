package helpers;

import dtos.CreateAccountRequest;
import dtos.TransferRequest;
import models.Account;
import models.AccountEvents;

import java.math.BigDecimal;

public class Fixtures {

    public static CreateAccountRequest createAccountRequest(String holder) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.holder = holder;
        request.firstDepositAmount = new BigDecimal("10.00");
        return request;
    }

    public static TransferRequest createTransferRequest(String from, String to, BigDecimal amount) {
        TransferRequest request = new TransferRequest();
        request.from = from;
        request.to = to;
        request.amount = amount;
        return request;
    }

    public static Account createAccount(String holder, BigDecimal initialDepositAmount) {
        Account account = new Account(holder);
        account.deposit(new AccountEvents.DepositEvent(initialDepositAmount));
        return account;
    }
}
