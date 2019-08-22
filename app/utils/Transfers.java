package utils;

import models.Account;

public class Transfers {

    public static Account primaryLock(Account a1, Account a2) {
        return (a1.getId().compareTo(a2.getId()) > 0) ? a1 : a2;
    }

    public static Account secondaryLock(Account a1, Account a2) {
        return (a1.getId().compareTo(a2.getId()) < 0) ? a2 : a1;
    }
}
