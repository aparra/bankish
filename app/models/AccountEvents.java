package models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountEvents {

    public interface AccountEvent {
        UUID getId();
    }

    public static class WithdrawEvent implements AccountEvent {

        private UUID id;
        private BigDecimal amount;

        public WithdrawEvent(BigDecimal amount) {
            this(UUID.randomUUID(), amount);
        }

        public WithdrawEvent(UUID id, BigDecimal amount) {
            this.id = id;
            this.amount = amount;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Positive
        public BigDecimal getAmount() {
            return amount;
        }
    }

    public static class DepositEvent implements AccountEvent {

        private UUID id;
        private BigDecimal amount;

        public DepositEvent(BigDecimal amount) {
            this(UUID.randomUUID(), amount);
        }

        public DepositEvent(UUID id, BigDecimal amount) {
            this.id = id;
            this.amount = amount;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Positive
        public BigDecimal getAmount() {
            return amount;
        }
    }


    public static class TransferEvent implements AccountEvent {

        private UUID id;
        private BigDecimal amount;
        private Account to;

        public TransferEvent(BigDecimal amount, Account to) {
            this(UUID.randomUUID(), amount, to);
        }

        public TransferEvent(UUID id, BigDecimal amount, Account to) {
            this.id = id;
            this.amount = amount;
            this.to = to;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Positive
        public BigDecimal getAmount() {
            return amount;
        }

        @NotNull
        public Account getTo() {
            return to;
        }
    }
}
