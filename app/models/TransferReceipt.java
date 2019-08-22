package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransferReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime at;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;

    private TransferReceipt(Builder builder) {
        this.at = builder.at;
        this.fromAccountId = builder.fromAccountId;
        this.toAccountId = builder.toAccountId;
        this.amount = builder.amount;
    }

    public LocalDateTime getAt() {
        return at;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private LocalDateTime at;
        private UUID fromAccountId;
        private UUID toAccountId;
        private BigDecimal amount;

        private Builder() { }

        public Builder at(LocalDateTime dateTime) {
            this.at = dateTime;
            return this;
        }

        public Builder fromAccountId(UUID fromAccountId) {
            this.fromAccountId = fromAccountId;
            return this;
        }

        public Builder toAccountId(UUID toAccountId) {
            this.toAccountId = toAccountId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferReceipt build() {
            return new TransferReceipt(this);
        }
    }
}
