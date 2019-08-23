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

        AccountEvents.DepositEvent depositEvent = new AccountEvents.DepositEvent(amount);
        account.deposit(depositEvent);
        assertEquals(amount, account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);
        assertEquals(depositEvent.getId(), transaction.getId());
        assertNotNull(transaction.getAt());
        assertEquals(TransactionType.CREDIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    public void should_successfully_make_a_withdraw() {
        Account account = new Account("Anderson");

        BigDecimal depositAmount = new BigDecimal("100.00");
        account.deposit(new AccountEvents.DepositEvent(depositAmount));

        BigDecimal withdrawAmount = new BigDecimal("50.00");
        AccountEvents.WithdrawEvent withdrawEvent = new AccountEvents.WithdrawEvent(withdrawAmount);
        account.withdraw(withdrawEvent);

        assertEquals(depositAmount.subtract(withdrawAmount), account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(2, transactions.size());

        Transaction transaction = transactions.stream().filter(t -> t.getType() == TransactionType.DEBIT).findFirst().get();
        assertEquals(withdrawEvent.getId(), transaction.getId());
        assertNotNull(transaction.getAt());
        assertEquals(withdrawAmount, transaction.getAmount());
    }

    @Test
    public void should_not_make_a_withdraw_when_there_is_no_enough_balance() {
        Account account = new Account("Anderson");

        BigDecimal depositAmount = new BigDecimal("100.00");
        account.deposit(new AccountEvents.DepositEvent(depositAmount));

        assertThat(exceptionOf(() -> account.withdraw(new AccountEvents.WithdrawEvent(new BigDecimal("150.00")))),
                instanceOf(Exceptions.InsufficientBalanceException.class));

        assertEquals(depositAmount, account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    public void should_successfully_make_a_transfer() {
        Account fromAccount =  Fixtures.createAccount("Anderson From", new BigDecimal("100.00"));
        Account toAccount =  Fixtures.createAccount("Anderson To", new BigDecimal("100.00"));

        AccountEvents.TransferEvent transferEvent = new AccountEvents.TransferEvent(new BigDecimal("50.00"), toAccount);
        fromAccount.transfer(transferEvent);

        assertEquals(new BigDecimal("50.00"), fromAccount.getBalance());
        assertEquals(2, fromAccount.getTransactions().size());
        Optional<Transaction> debitTransactionFromTransfer = fromAccount.getTransactions().stream().filter(t -> t.getType() == TransactionType.DEBIT).findFirst();
        assertTrue(debitTransactionFromTransfer.isPresent());
        assertEquals(transferEvent.getId(), debitTransactionFromTransfer.get().getId());

        assertEquals(new BigDecimal("150.00"), toAccount.getBalance());
        assertEquals(2, toAccount.getTransactions().size());
        long totalCreditTransactions = toAccount.getTransactions().stream().filter(t -> t.getType() == TransactionType.CREDIT).count();
        assertEquals(2, totalCreditTransactions);
        Optional<Transaction> creditTransactionFromTransfer = toAccount.getTransactions().stream().filter(t -> t.getId() == transferEvent.getId()).findFirst();
        assertTrue(creditTransactionFromTransfer.isPresent());
    }

    @Test
    public void should_not_make_a_transfer_when_account_from_there_is_no_enough_balance() {
        Account fromAccount =  Fixtures.createAccount("Anderson From", new BigDecimal("100.00"));
        Account toAccount =  Fixtures.createAccount("Anderson To", new BigDecimal("100.00"));

        assertThat(exceptionOf(() -> fromAccount.transfer(new AccountEvents.TransferEvent(new BigDecimal("150.00"), toAccount))),
                instanceOf(Exceptions.InsufficientBalanceException.class));

        assertEquals(new BigDecimal("100.00"), fromAccount.getBalance());
        assertEquals(1, fromAccount.getTransactions().size());

        assertEquals(new BigDecimal("100.00"), toAccount.getBalance());
        assertEquals(1, toAccount.getTransactions().size());
    }
}
