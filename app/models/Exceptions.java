package models;

import java.util.UUID;

public class Exceptions {

    public static class InsufficientBalanceException extends RuntimeException {

        private static String message = "Insufficient balance in %s";

        public InsufficientBalanceException(UUID accountId) {
            super(String.format(message, accountId.toString()));
        }
    }

    public static class AccountNotFoundException extends RuntimeException {

        private static String message = "Account %s was not found";

        public AccountNotFoundException(String accountId) {
            super(String.format(message, accountId));
        }
    }
}
