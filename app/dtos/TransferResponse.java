package dtos;

import models.TransferReceipt;

import static utils.Dates.format;

public final class TransferResponse {
    public String at;
    public String from;
    public String to;
    public String amount;

    public static final class Builder {

        public TransferResponse from(TransferReceipt receipt) {
            TransferResponse response = new TransferResponse();
            response.at = format(receipt.getAt());
            response.from = receipt.getFromAccountId().toString();
            response.to = receipt.getToAccountId().toString();
            response.amount = receipt.getAmount().setScale(2).toPlainString();
            return response;
        }
    }
}
