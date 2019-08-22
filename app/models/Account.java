package models;

import utils.Transfers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String holder;
    private List<Transaction> transactions;
    private BigDecimal balance;

    public Account(String holder) {
        this.id = UUID.randomUUID();
        this.holder = holder;

        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;
    }

    public UUID getId() {
        return this.id;
    }

    public String getHolder() {
        return this.holder;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal withdraw(BigDecimal amount) throws Exceptions.InsufficientBalanceException {
        if (!canWithdraw(amount)) {
            throw new Exceptions.InsufficientBalanceException(this.id);
        }
        balance = balance.subtract(amount);
        transactions.add(Transaction.builder().at(now()).debit(amount).build());
        return amount;
    }

    private boolean canWithdraw(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
        Transaction t = Transaction.builder().at(now()).credit(amount).build();
        transactions.add(Transaction.builder().at(now()).credit(amount).build());
    }

    public TransferReceipt transfer(BigDecimal amount, Account to) throws Exceptions.InsufficientBalanceException {
        synchronized (Transfers.primaryLock(this, to)) {
            synchronized (Transfers.secondaryLock(this, to)) {
                this.withdraw(amount);
                to.deposit(amount);
                return TransferReceipt.builder().at(now()).fromAccountId(this.id).toAccountId(to.id).amount(amount).build();
            }
        }
    }
}
