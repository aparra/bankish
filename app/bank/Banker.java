package bank;

import dtos.AccountResponse;
import dtos.CreateAccountRequest;
import dtos.TransferRequest;
import dtos.TransferResponse;
import models.Account;
import models.Exceptions;
import models.TransferReceipt;
import repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Singleton
public class Banker {

    private AccountRepository accountRepository;

    @Inject
    public Banker(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CompletionStage<AccountResponse> createAccount(CreateAccountRequest request) {
        Account account = new Account(request.holder);
        account.deposit(request.firstDepositAmount);
        return accountRepository.store(account).thenApply(a -> new AccountResponse.Builder().from(a));
    }

    public CompletionStage<TransferResponse> transfer(TransferRequest request) {
        return this.getAccountBy(request.from).thenCombineAsync(this.getAccountBy(request.to), (from, to) -> {
            TransferReceipt receipt = from.transfer(request.amount, to);
            return new TransferResponse.Builder().from(receipt);
        });
    }

    private CompletionStage<Account> getAccountBy(String id) {
        return accountRepository
                .getBy(UUID.fromString(id))
                .thenApply(maybeAccount -> maybeAccount.orElseThrow(() -> new Exceptions.AccountNotFoundException(id)));
    }
}

