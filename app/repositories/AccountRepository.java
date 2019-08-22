package repositories;

import models.Account;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class AccountRepository {

    private Map<UUID, Account> bank = new HashMap<>();

    public CompletionStage<Account> store(Account account) {
        return CompletableFuture.supplyAsync(() -> {
            bank.put(account.getId(), account);
            return account;
        });
    }

    public CompletionStage<Optional<Account>> getBy(UUID id) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(bank.get(id)));
    }
}
