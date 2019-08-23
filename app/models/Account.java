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

    public UUID withdraw(AccountEvents.WithdrawEvent event) throws Exceptions.InsufficientBalanceException {
        if (!canWithdraw(event.getAmount())) {
            throw new Exceptions.InsufficientBalanceException(this.id);
        }
        balance = balance.subtract(event.getAmount());
        transactions.add(Transaction.builder().id(event.getId()).at(now()).debit(event.getAmount()).build());
        return event.getId();
    }

    private boolean canWithdraw(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    public void deposit(AccountEvents.DepositEvent event) {
        balance = balance.add(event.getAmount());
        transactions.add(Transaction.builder().id(event.getId()).at(now()).credit(event.getAmount()).build());
    }

    public TransferReceipt transfer(AccountEvents.TransferEvent event) throws Exceptions.InsufficientBalanceException {
        synchronized (Transfers.primaryLock(this, event.getTo())) {
            synchronized (Transfers.secondaryLock(this, event.getTo())) {
                AccountEvents.WithdrawEvent withdrawEvent = new AccountEvents.WithdrawEvent(event.getId(), event.getAmount());
                this.withdraw(withdrawEvent);

                AccountEvents.DepositEvent depositEvent = new AccountEvents.DepositEvent(event.getId(), event.getAmount());
                event.getTo().deposit(depositEvent);

                return TransferReceipt.builder()
                        .at(now())
                        .fromAccountId(this.id)
                        .toAccountId(event.getTo().id)
                        .amount(event.getAmount())
                        .build();
            }
        }
    }
}
