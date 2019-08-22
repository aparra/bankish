package dtos;

import models.Account;

public final class AccountResponse {
    public String id;
    public String holder;
    public String balance;

    public static final class Builder {

        public AccountResponse from(Account account) {
            AccountResponse response = new AccountResponse();
            response.id = account.getId().toString();
            response.holder = account.getHolder();
            response.balance = account.getBalance().setScale(2).toPlainString();
            return response;
        }
    }
}
