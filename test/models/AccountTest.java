package models;

import static helpers.TestHelpers.exceptionOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import helpers.Fixtures;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void should_successfully_make_a_deposit() {
        Account account = new Account("Anderson");
        BigDecimal amount = new BigDecimal("100.00");

        account.deposit(amount);
        assertEquals(amount, account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);
        assertNotNull(transaction.getAt());
        assertEquals(TransactionType.CREDIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    public void should_successfully_make_a_withdraw() {
        Account account = new Account("Anderson");

        BigDecimal depositAmount = new BigDecimal("100.00");
        account.deposit(depositAmount);

        BigDecimal withdrawAmount = new BigDecimal("50.00");
        account.withdraw(withdrawAmount);

        assertEquals(depositAmount.subtract(withdrawAmount), account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(2, transactions.size());

        Transaction transaction = transactions.stream().filter(t -> t.getType() == TransactionType.DEBIT).findFirst().get();
        assertNotNull(transaction.getAt());
        assertEquals(withdrawAmount, transaction.getAmount());
    }

    @Test
    public void should_not_make_a_withdraw_when_there_is_no_enough_balance() {
        Account account = new Account("Anderson");

        BigDecimal depositAmount = new BigDecimal("100.00");
        account.deposit(depositAmount);

        assertThat(exceptionOf(() -> account.withdraw(new BigDecimal("150.00"))),
                instanceOf(Exceptions.InsufficientBalanceException.class));

        assertEquals(depositAmount, account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    public void should_successfully_make_a_transfer() {
        Account fromAccount =  Fixtures.createAccount("Anderson From", new BigDecimal("100.00"));
        Account toAccount =  Fixtures.createAccount("Anderson To", new BigDecimal("100.00"));

        fromAccount.transfer(new BigDecimal("50.00"), toAccount);

        assertEquals(new BigDecimal("50.00"), fromAccount.getBalance());
        assertEquals(2, fromAccount.getTransactions().size());
        Optional<Transaction> debitTransaction = fromAccount.getTransactions().stream().filter(t -> t.getType() == TransactionType.DEBIT).findFirst();
        assertTrue(debitTransaction.isPresent());

        assertEquals(new BigDecimal("150.00"), toAccount.getBalance());
        assertEquals(2, toAccount.getTransactions().size());
        long totalCreditTransactions = toAccount.getTransactions().stream().filter(t -> t.getType() == TransactionType.CREDIT).count();
        assertEquals(2, totalCreditTransactions);
    }

    @Test
    public void should_not_make_a_transfer_when_account_from_there_is_no_enough_balance() {
        Account fromAccount =  Fixtures.createAccount("Anderson From", new BigDecimal("100.00"));
        Account toAccount =  Fixtures.createAccount("Anderson To", new BigDecimal("100.00"));

        assertThat(exceptionOf(() -> fromAccount.transfer(new BigDecimal("150.00"), toAccount)),
                instanceOf(Exceptions.InsufficientBalanceException.class));

        assertEquals(new BigDecimal("100.00"), fromAccount.getBalance());
        assertEquals(1, fromAccount.getTransactions().size());

        assertEquals(new BigDecimal("100.00"), toAccount.getBalance());
        assertEquals(1, toAccount.getTransactions().size());
    }
}
