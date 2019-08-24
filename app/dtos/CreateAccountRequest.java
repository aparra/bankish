package dtos;

import play.data.validation.Constraints;

import java.math.BigDecimal;

public final class CreateAccountRequest {

    @Constraints.Required
    private String holder;

    @Constraints.Required
    private BigDecimal firstDepositAmount;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public BigDecimal getFirstDepositAmount() {
        return firstDepositAmount;
    }

    public void setFirstDepositAmount(BigDecimal firstDepositAmount) {
        this.firstDepositAmount = firstDepositAmount;
    }
}
