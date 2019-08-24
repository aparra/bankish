package dtos;

import play.data.validation.Constraints;

import java.math.BigDecimal;

public final class TransferRequest {

    @Constraints.Required
    public String from;

    @Constraints.Required
    public String to;

    @Constraints.Required @Constraints.Min(value = 1)
    public BigDecimal amount;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
