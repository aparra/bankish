package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private LocalDateTime at;
    private BigDecimal amount;
    private TransactionType type;

    private Transaction(Builder builder) {
        this.id = builder.id;
        this.at = builder.at;
        this.amount = builder.amount;
        this.type = builder.type;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getAt() {
        return at;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID id;
        private LocalDateTime at;
        private BigDecimal amount;
        private TransactionType type;

        private Builder() { }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder at(LocalDateTime dateTime) {
            this.at = dateTime;
            return this;
        }

        public Builder credit(BigDecimal amount) {
            return transaction(TransactionType.CREDIT, amount);
        }

        public Builder debit(BigDecimal amount) {
            return transaction(TransactionType.DEBIT, amount);
        }

        private Builder transaction(TransactionType type, BigDecimal amount) {
            this.type = type;
            this.amount = amount;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
